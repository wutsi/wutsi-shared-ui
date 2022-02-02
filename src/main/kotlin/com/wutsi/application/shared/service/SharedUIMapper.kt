package com.wutsi.application.shared.service

import com.wutsi.application.shared.entity.CategoryEntity
import com.wutsi.application.shared.entity.CityEntity
import com.wutsi.application.shared.model.AccountModel
import com.wutsi.application.shared.model.PaymentMethodModel
import com.wutsi.application.shared.model.PictureModel
import com.wutsi.application.shared.model.PriceModel
import com.wutsi.application.shared.model.ProductModel
import com.wutsi.application.shared.model.SavingsModel
import com.wutsi.application.shared.model.TransactionModel
import com.wutsi.platform.account.dto.Account
import com.wutsi.platform.account.dto.AccountSummary
import com.wutsi.platform.account.dto.PaymentMethodSummary
import com.wutsi.platform.catalog.dto.PictureSummary
import com.wutsi.platform.catalog.dto.Product
import com.wutsi.platform.catalog.dto.ProductSummary
import com.wutsi.platform.payment.dto.TransactionSummary
import com.wutsi.platform.tenant.dto.Tenant
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

open class SharedUIMapper(
    private val categoryService: CategoryService,
    private val cityService: CityService,
) {
    open fun toProductModel(obj: ProductSummary, tenant: Tenant) = ProductModel(
        id = obj.id,
        title = obj.title,
        summary = obj.summary,
        price = obj.price?.let { toPriceModel(it, tenant) },
        comparablePrice = toComparablePrice(obj.price, obj.comparablePrice, tenant),
        savings = toSavings(obj.price, obj.comparablePrice),
        thumbnail = toPictureModel(obj.thumbnail, tenant.product.defaultPictureUrl)
    )

    open fun toProductModel(obj: Product, tenant: Tenant) = ProductModel(
        id = obj.id,
        title = obj.title,
        price = obj.price?.let { toPriceModel(it, tenant) },
        comparablePrice = toComparablePrice(obj.price, obj.comparablePrice, tenant),
        savings = toSavings(obj.price, obj.comparablePrice),
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

    private fun toSavings(price: Double?, comparablePrice: Double?): SavingsModel? {
        if (comparablePrice != null && price != null) {
            val value = comparablePrice - price
            if (value > 0)
                return SavingsModel(
                    value = value,
                    percent = if (comparablePrice == 0.0) null else (value * 100.0 / comparablePrice).toInt()
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
        category = obj.categoryId?.let { toCategoryText(it) }
    )

    open fun toAccountModel(obj: Account) = AccountModel(
        id = obj.id,
        displayName = obj.displayName,
        pictureUrl = obj.pictureUrl,
        website = obj.website,
        business = obj.business,
        retail = obj.retail,
        location = toLocationText(cityService.get(obj.cityId), obj.country),
        category = obj.categoryId?.let { toCategoryText(it) },
        phoneNumber = PhoneUtil.format(obj.phone?.number, obj.phone?.country),
        biography = obj.biography
    )

    open fun toTransactionModel(
        obj: TransactionSummary,
        currentUser: Account?,
        accounts: Map<Long, AccountSummary>,
        paymentMethod: PaymentMethodSummary?,
        tenant: Tenant,
        tenantProvider: TenantProvider,
        messageSource: MessageSource
    ): TransactionModel {
        val fmt = DecimalFormat(tenant.monetaryFormat)
        val locale = LocaleContextHolder.getLocale()
        val timezoneId = currentUser?.timezoneId
        return TransactionModel(
            id = obj.id,
            type = obj.type,
            status = obj.status,
            statusText = getText("transaction.status.${obj.status}", messageSource),
            amountText = fmt.format(obj.amount),
            netText = fmt.format(obj.net),
            feesText = fmt.format(obj.fees),
            recipient = accounts[obj.recipientId]?.let { toAccountModel(it) },
            account = accounts[obj.accountId]?.let { toAccountModel(it) },
            paymentMethod = paymentMethod?.let { toPaymentMethodModel(it, tenant, tenantProvider) },
            description = toDescription(obj, currentUser?.id, messageSource),
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
        messageSource: MessageSource
    ): String =
        if (obj.type == "CASHIN") {
            getText("transaction.type.CASHIN", messageSource)
        } else if (obj.type == "CASHOUT") {
            getText("transaction.type.CASHOUT", messageSource)
        } else if (obj.type == "PAYMENT") {
            getText("transaction.type.PAYMENT", messageSource)
        } else {
            if (obj.recipientId == currentUserId)
                getText("transaction.type.TRANSFER.receive", messageSource)
            else
                getText("transaction.type.TRANSFER.send", messageSource)
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

    open fun toCategoryText(categoryId: Long): String? {
        val category = categoryService.get(categoryId)
        return toCategoryText(category)
    }

    open fun toCategoryText(category: CategoryEntity?): String? {
        if (category != null) {
            val locale = LocaleContextHolder.getLocale()
            if (locale.language == "fr")
                return category.titleFrench
            else
                return category.title
        } else
            return null
    }

    private fun getText(key: String, messageSource: MessageSource): String {
        return try {
            val locale = LocaleContextHolder.getLocale()
            messageSource.getMessage(key, emptyArray(), locale)
        } catch (ex: Exception) {
            key
        }
    }
}
