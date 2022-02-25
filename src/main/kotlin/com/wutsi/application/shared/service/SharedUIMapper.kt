package com.wutsi.application.shared.service

import com.wutsi.application.shared.entity.CityEntity
import com.wutsi.application.shared.model.AccountModel
import com.wutsi.application.shared.model.CartItemModel
import com.wutsi.application.shared.model.CategoryModel
import com.wutsi.application.shared.model.OrderItemModel
import com.wutsi.application.shared.model.PaymentMethodModel
import com.wutsi.application.shared.model.PictureModel
import com.wutsi.application.shared.model.PriceModel
import com.wutsi.application.shared.model.PriceSummaryModel
import com.wutsi.application.shared.model.ProductModel
import com.wutsi.application.shared.model.SavingsModel
import com.wutsi.application.shared.model.TransactionModel
import com.wutsi.application.shared.service.TransactionUtil.getText
import com.wutsi.ecommerce.cart.dto.Cart
import com.wutsi.ecommerce.catalog.dto.CategorySummary
import com.wutsi.ecommerce.catalog.dto.PictureSummary
import com.wutsi.ecommerce.catalog.dto.Product
import com.wutsi.ecommerce.catalog.dto.ProductSummary
import com.wutsi.ecommerce.order.dto.Order
import com.wutsi.ecommerce.order.dto.OrderItem
import com.wutsi.platform.account.dto.Account
import com.wutsi.platform.account.dto.AccountSummary
import com.wutsi.platform.account.dto.Category
import com.wutsi.platform.account.dto.PaymentMethodSummary
import com.wutsi.platform.payment.dto.TransactionSummary
import com.wutsi.platform.tenant.dto.Tenant
import org.springframework.context.i18n.LocaleContextHolder
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

open class SharedUIMapper(
    private val cityService: CityService,
) {
    open fun toPriceSummaryModel(obj: Order, tenant: Tenant): PriceSummaryModel = PriceSummaryModel(
        itemCount = obj.items.size,
        subTotal = toPriceModel(obj.subTotalPrice, tenant),
        total = toPriceModel(obj.totalPrice, tenant),
        deliveryFees = null,
        savings = toSavings(obj.totalPrice, obj.subTotalPrice, tenant)
    )

    open fun toPriceSummaryModel(obj: Cart, products: List<ProductSummary>, tenant: Tenant): PriceSummaryModel {
        val subTotal = products.sumOf { getQuantity(obj, it.id) * (it.comparablePrice ?: it.price ?: 0.0) }
        val total = products.sumOf { getQuantity(obj, it.id) * (it.price ?: 0.0) }

        return PriceSummaryModel(
            itemCount = obj.products.size,
            subTotal = toPriceModel(subTotal, tenant),
            total = toPriceModel(total, tenant),
            deliveryFees = null,
            savings = toSavings(total, subTotal, tenant)
        )
    }

    private fun getQuantity(cart: Cart, productId: Long): Int =
        cart.products.find { it.productId == productId }?.quantity ?: 0

    open fun toCartItemModel(
        obj: com.wutsi.ecommerce.cart.dto.Product,
        product: ProductSummary,
        tenant: Tenant,
    ): CartItemModel {
        val price = product.price ?: 0.0
        return CartItemModel(
            title = product.title,
            unitPrice = toPriceModel(product.price!!, tenant),
            price = toPriceModel(price * obj.quantity, tenant),
            comparablePrice = product.comparablePrice?.let {
                toComparablePrice(
                    price * obj.quantity,
                    it * obj.quantity,
                    tenant
                )
            },
            savings = toSavings(obj.quantity * price, product.comparablePrice?.let { obj.quantity * it }, tenant),
            thumbnail = toPictureModel(product.thumbnail, tenant.product.defaultPictureUrl),
            quantity = obj.quantity,
            quantityInStock = product.quantity,
            maxQuantity = if (product.maxOrder == null || product.maxOrder == 0) obj.quantity else product.maxOrder!!
        )
    }

    open fun toOrderItemModel(
        obj: OrderItem,
        product: ProductSummary,
        tenant: Tenant,
    ) = OrderItemModel(
        title = product.title,
        unitPrice = toPriceModel(obj.unitPrice, tenant),
        price = toPriceModel(obj.unitPrice * obj.quantity, tenant),
        comparablePrice = obj.unitComparablePrice?.let {
            toComparablePrice(
                obj.unitPrice * obj.quantity,
                it * obj.quantity,
                tenant
            )
        },
        savings = toSavings(obj.quantity * obj.unitPrice, obj.unitComparablePrice?.let { obj.quantity * it }, tenant),
        thumbnail = toPictureModel(product.thumbnail, tenant.product.defaultPictureUrl),
        quantity = obj.quantity
    )

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

    fun toPriceModel(amount: Double, tenant: Tenant): PriceModel =
        PriceModel(
            amount = amount,
            currency = tenant.currency,
            text = DecimalFormat(tenant.monetaryFormat).format(amount)
        )

    fun toSavings(
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

    open fun toCategoryModel(category: CategorySummary?): CategoryModel? =
        category?.let {
            CategoryModel(
                id = category.id,
                title = it.title
            )
        }

    open fun toCategoryModel(category: Category?): CategoryModel? =
        category?.let {
            CategoryModel(
                id = category.id,
                title = it.title
            )
        }
}
