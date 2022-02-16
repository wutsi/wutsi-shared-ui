package com.wutsi.application.shared.service

import com.wutsi.application.shared.entity.CityEntity
import com.wutsi.application.shared.model.AccountModel
import com.wutsi.application.shared.model.CategoryModel
import com.wutsi.application.shared.model.PaymentMethodModel
import com.wutsi.application.shared.model.PictureModel
import com.wutsi.application.shared.model.PriceModel
import com.wutsi.application.shared.model.ProductModel
import com.wutsi.application.shared.model.SavingsModel
import com.wutsi.application.shared.model.TransactionModel
import com.wutsi.platform.account.WutsiAccountApi
import com.wutsi.platform.account.dto.Account
import com.wutsi.platform.account.dto.AccountSummary
import com.wutsi.platform.account.dto.Category
import com.wutsi.platform.account.dto.PaymentMethodSummary
import com.wutsi.platform.catalog.dto.PictureSummary
import com.wutsi.platform.catalog.dto.Product
import com.wutsi.platform.catalog.dto.ProductSummary
import com.wutsi.platform.payment.dto.TransactionSummary
import com.wutsi.platform.tenant.dto.Tenant
import feign.FeignException
import org.slf4j.LoggerFactory
import org.springframework.context.i18n.LocaleContextHolder
import java.text.DecimalFormat
import java.text.MessageFormat
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.ResourceBundle

open class SharedUIMapper(
    private val accountApi: WutsiAccountApi,
    private val cityService: CityService,
) {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(SharedUIMapper::class.java)
    }

    private val defaultBundle = ResourceBundle.getBundle("shared-ui-messages")
    private val frBundle = ResourceBundle.getBundle("shared-ui-messages", Locale("fr"))

    open fun toProductModel(
        obj: ProductSummary,
        tenant: Tenant,
    ) = ProductModel(
        id = obj.id,
        title = obj.title,
        summary = obj.summary,
        price = obj.price?.let { toPriceModel(it, tenant) },
        comparablePrice = toComparablePrice(obj.price, obj.comparablePrice, tenant),
        savings = toSavings(obj.price, obj.comparablePrice, tenant),
        thumbnail = toPictureModel(obj.thumbnail, tenant.product.defaultPictureUrl)
    )

    open fun toProductModel(
        obj: Product,
        tenant: Tenant,
    ) = ProductModel(
        id = obj.id,
        title = obj.title,
        summary = obj.summary,
        price = obj.price?.let { toPriceModel(it, tenant) },
        comparablePrice = toComparablePrice(obj.price, obj.comparablePrice, tenant),
        savings = toSavings(obj.price, obj.comparablePrice, tenant),
        thumbnail = toPictureModel(obj.thumbnail, tenant.product.defaultPictureUrl),
        pictures = obj.pictures.map { toPictureModel(it, tenant.product.defaultPictureUrl) },
        visible = obj.visible
    )

    private fun toComparablePrice(price: Double?, comparablePrice: Double?, tenant: Tenant): PriceModel? {
        if (price == null || comparablePrice == null)
            return null

        if (price >= comparablePrice)
            return null

        return toPriceModel(comparablePrice, tenant)
    }

    private fun toPriceModel(amount: Double, tenant: Tenant): PriceModel =
        PriceModel(
            amount = amount,
            currency = tenant.currency,
            text = DecimalFormat(tenant.monetaryFormat).format(amount)
        )

    private fun toSavings(
        price: Double?,
        comparablePrice: Double?,
        tenant: Tenant
    ): SavingsModel? {
        if (comparablePrice != null && price != null) {
            val value = comparablePrice - price
            val percent = if (comparablePrice == 0.0) null else (value * 100.0 / comparablePrice).toInt()
            if (value > 0)
                return SavingsModel(
                    value = value,
                    percent = percent,
                    text = DecimalFormat(tenant.monetaryFormat).format(value),
                    percentText = getText(
                        "shared-ui.product.saving.percentage",
                        arrayOf(percent.toString())
                    )
                )
        }
        return null
    }

    private fun toPictureModel(obj: PictureSummary?, defaultPictureUrl: String) = PictureModel(
        url = obj?.url ?: defaultPictureUrl
    )

    open fun toAccountModel(obj: AccountSummary) = AccountModel(
        id = obj.id,
        displayName = obj.displayName,
        pictureUrl = obj.pictureUrl,
        business = obj.business,
        retail = obj.retail,
        location = toLocationText(null, obj.country),
        category = toCategoryModel(obj.categoryId),
        businessText = toBusinessText(obj.business, obj.retail)
    )

    open fun toAccountModel(obj: Account) = AccountModel(
        id = obj.id,
        displayName = obj.displayName,
        pictureUrl = obj.pictureUrl,
        website = obj.website,
        business = obj.business,
        retail = obj.retail,
        location = toLocationText(cityService.get(obj.cityId), obj.country),
        category = toCategoryModel(obj.category),
        phoneNumber = PhoneUtil.format(obj.phone?.number, obj.phone?.country),
        biography = obj.biography,
        businessText = toBusinessText(obj.business, obj.retail)
    )

    private fun toBusinessText(business: Boolean, retail: Boolean): String? {
        if (!business && !retail)
            return null

        val text = StringBuilder()
        if (business)
            text.append(getText("shared-ui.account.business"))

        if (retail) {
            if (text.isNotEmpty())
                text.append(" - ")
            text.append(getText("shared-ui.account.retail"))
        }
        return text.toString()
    }

    open fun toTransactionModel(
        obj: TransactionSummary,
        currentUser: Account?,
        accounts: Map<Long, AccountSummary>,
        paymentMethod: PaymentMethodSummary?,
        tenant: Tenant,
        tenantProvider: TenantProvider,
    ): TransactionModel {
        val fmt = DecimalFormat(tenant.monetaryFormat)
        val locale = LocaleContextHolder.getLocale()
        val timezoneId = currentUser?.timezoneId
        return TransactionModel(
            id = obj.id,
            type = obj.type,
            status = obj.status,
            statusText = getText("shared-ui.transaction.status.${obj.status}"),
            amountText = fmt.format(obj.amount),
            netText = fmt.format(obj.net),
            feesText = fmt.format(obj.fees),
            recipient = accounts[obj.recipientId]?.let { toAccountModel(it) },
            account = accounts[obj.accountId]?.let { toAccountModel(it) },
            paymentMethod = paymentMethod?.let { toPaymentMethodModel(it, tenant, tenantProvider) },
            description = toDescription(obj, currentUser?.id),
            createdText = DateTimeUtil.convert(obj.created, timezoneId)
                .format(DateTimeFormatter.ofPattern(tenant.dateFormat, locale)),
            currentUserId = currentUser?.id ?: -1
        )
    }

    open fun toPaymentMethodModel(
        obj: PaymentMethodSummary,
        tenant: Tenant,
        tenantProvider: TenantProvider
    ): PaymentMethodModel? {
        val carrier = tenant.mobileCarriers.find { it.code.equals(obj.provider, true) }
            ?: return null

        return PaymentMethodModel(
            token = obj.token,
            phoneNumber = obj.phone?.number ?: obj.maskedNumber,
            iconUrl = tenantProvider.logo(carrier) ?: ""
        )
    }

    private fun toDescription(
        obj: TransactionSummary,
        currentUserId: Long?,
    ): String =
        if (obj.type == "CASHIN") {
            getText("shared-ui.transaction.type.CASHIN")
        } else if (obj.type == "CASHOUT") {
            getText("shared-ui.transaction.type.CASHOUT")
        } else if (obj.type == "PAYMENT") {
            getText("shared-ui.transaction.type.PAYMENT")
        } else {
            if (obj.recipientId == currentUserId)
                getText("shared-ui.transaction.type.TRANSFER.receive")
            else
                getText("shared-ui.transaction.type.TRANSFER.send")
        }

    open fun toLocationText(city: CityEntity?, country: String): String {
        val locale = LocaleContextHolder.getLocale()
        if (city != null) {
            val country = StringUtil.capitalizeFirstLetter(
                Locale(locale.language, city.country).getDisplayCountry(locale)
            )
            return "${city.name}, $country"
        }
        return StringUtil.capitalizeFirstLetter(Locale(locale.language, country).getDisplayCountry(locale))
    }

    open fun toCategoryModel(category: Category?): CategoryModel? =
        category?.let {
            CategoryModel(
                id = category.id,
                title = it.title
            )
        }

    open fun toCategoryModel(id: Long?): CategoryModel? {
        if (id == null)
            return null

        return try {
            val category = accountApi.getCategory(id).category
            toCategoryModel(category)
        } catch (ex: FeignException) {
            LOGGER.warn("Unable to resolve Category#$id", ex)
            null
        }
    }

    private fun getText(key: String, args: Array<String> = emptyArray()): String {
        return try {
            val locale = LocaleContextHolder.getLocale()
            val bundle = if (locale.language == "fr")
                frBundle
            else
                defaultBundle

            return bundle.getString(MessageFormat.format(key, args))
        } catch (ex: Exception) {
            key
        }
    }
}
