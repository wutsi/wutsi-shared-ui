package com.wutsi.application.shared.service

import com.wutsi.application.shared.entity.CityEntity
import com.wutsi.application.shared.model.AccountModel
import com.wutsi.application.shared.model.AddressModel
import com.wutsi.application.shared.model.BottomNavigationBarModel
import com.wutsi.application.shared.model.CartItemModel
import com.wutsi.application.shared.model.CategoryModel
import com.wutsi.application.shared.model.OrderItemModel
import com.wutsi.application.shared.model.PaymentMethodModel
import com.wutsi.application.shared.model.PictureModel
import com.wutsi.application.shared.model.PriceModel
import com.wutsi.application.shared.model.PriceSummaryModel
import com.wutsi.application.shared.model.ProductModel
import com.wutsi.application.shared.model.SavingsModel
import com.wutsi.application.shared.model.ShippingModel
import com.wutsi.application.shared.model.TransactionModel
import com.wutsi.application.shared.service.TranslationUtil.getText
import com.wutsi.ecommerce.cart.dto.Cart
import com.wutsi.ecommerce.catalog.dto.CategorySummary
import com.wutsi.ecommerce.catalog.dto.PictureSummary
import com.wutsi.ecommerce.catalog.dto.Product
import com.wutsi.ecommerce.catalog.dto.ProductSummary
import com.wutsi.ecommerce.order.dto.Address
import com.wutsi.ecommerce.order.dto.Order
import com.wutsi.ecommerce.order.dto.OrderItem
import com.wutsi.ecommerce.order.entity.AddressType
import com.wutsi.ecommerce.order.entity.PaymentStatus
import com.wutsi.ecommerce.shipping.dto.Shipping
import com.wutsi.platform.account.dto.Account
import com.wutsi.platform.account.dto.AccountSummary
import com.wutsi.platform.account.dto.Category
import com.wutsi.platform.account.dto.PaymentMethodSummary
import com.wutsi.platform.core.image.Dimension
import com.wutsi.platform.core.image.Focus
import com.wutsi.platform.core.image.ImageService
import com.wutsi.platform.core.image.Transformation
import com.wutsi.platform.payment.dto.TransactionSummary
import com.wutsi.platform.tenant.dto.Tenant
import com.wutsi.platform.tenant.entity.ToggleName
import org.springframework.context.i18n.LocaleContextHolder
import java.lang.Integer.min
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

open class SharedUIMapper(
    private val cityService: CityService,
    private val imageService: ImageService,
) {
    companion object {
        private const val THUMBNAIL_HEIGHT = 250
        private const val IMAGE_HEIGHT = 450
    }

    open fun toBottomNavigationBarModel(
        shellUrl: String,
        cashUrl: String,
        togglesProvider: TogglesProvider,
        urlBuilder: URLBuilder
    ) = BottomNavigationBarModel(
        profileUrl = toUrl(shellUrl, "profile", urlBuilder),
        settingsUrl = toUrl(shellUrl, "settings", urlBuilder),
        transactionUrl = if (togglesProvider.isToggleEnabled(ToggleName.TRANSACTION_HISTORY))
            toUrl(cashUrl, "history", urlBuilder)
        else
            null
    )

    private fun toUrl(baseUrl: String, path: String, urlBuilder: URLBuilder): String =
        if (baseUrl.isNullOrEmpty())
            urlBuilder.build(path)
        else
            urlBuilder.build(baseUrl, path)

    open fun toShippingModel(order: Order, shipping: Shipping, tenant: Tenant): ShippingModel {
        val locale = LocaleContextHolder.getLocale()

        return ShippingModel(
            type = getText("shipping.type.${shipping.type}"),
            expectedDelivered = order.expectedDelivered?.let {
                DateTimeFormatter.ofPattern(tenant.dateFormat, locale).format(it)
            },
            message = shipping.message,
            rate = if (shipping.rate == null || shipping.rate == 0.0)
                getText("label.free")
            else
                DecimalFormat(tenant.monetaryFormat).format(shipping.rate!!)
        )
    }

    open fun toAddressModel(address: Address): AddressModel {
        if (address.type == AddressType.EMAIL.name)
            return AddressModel(
                fullName = "${address.firstName} ${address.lastName}".trim(),
                email = address.email,
                type = AddressType.EMAIL,
            )

        val locale = LocaleContextHolder.getLocale()
        val city = address.cityId?.let { cityService.get(it) }
        val country = Locale("en", city?.country ?: address.country).getDisplayCountry(locale)

        return AddressModel(
            fullName = "${address.firstName} ${address.lastName}".trim(),
            street = if (address.street.isNullOrEmpty()) null else address.street,
            location = city?.let { "${city.name}, $country" } ?: country,
            zipCode = address.zipCode,
            email = address.email,
            type = AddressType.valueOf(address.type),
        )
    }

    open fun toPriceSummaryModel(obj: Order, tenant: Tenant): PriceSummaryModel = PriceSummaryModel(
        itemCount = obj.items.size,
        subTotal = toPriceModel(obj.subTotalPrice, tenant),
        total = toPriceModel(obj.totalPrice, tenant),
        deliveryFees = obj.shippingId?.let { toPriceModel(obj.deliveryFees, tenant) },
        savings = toSavings(obj.totalPrice, obj.subTotalPrice, tenant),
        paid = obj.paymentStatus == PaymentStatus.PAID.name,
        totalPaid = toPriceModel(obj.totalPaid, tenant),
        balance = toPriceModel(obj.totalPrice - obj.totalPaid, tenant)
    )

    open fun toPriceSummaryModel(obj: Cart, products: List<ProductSummary>, tenant: Tenant): PriceSummaryModel {
        val subTotal = products.sumOf { getQuantity(obj, it.id) * (it.comparablePrice ?: it.price ?: 0.0) }
        val total = products.sumOf { getQuantity(obj, it.id) * (it.price ?: 0.0) }

        return PriceSummaryModel(
            itemCount = obj.products.size,
            subTotal = toPriceModel(subTotal, tenant),
            total = toPriceModel(total, tenant),
            deliveryFees = null,
            savings = toSavings(total, subTotal, tenant),
            paid = false,
            totalPaid = PriceModel(),
            balance = toPriceModel(total, tenant),
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
            thumbnail = toPictureModel(
                product.thumbnail,
                tenant.product.defaultPictureUrl,
                height = THUMBNAIL_HEIGHT
            ),
            quantity = obj.quantity,
            quantityInStock = product.quantity,
            maxQuantity = if (product.maxOrder == null || product.maxOrder == 0)
                min(product.quantity, 10)
            else
                product.maxOrder!!
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
        thumbnail = toPictureModel(
            obj = product.thumbnail,
            defaultPictureUrl = tenant.product.defaultPictureUrl,
            height = THUMBNAIL_HEIGHT
        ),
        quantity = obj.quantity
    )

    open fun toProductModel(
        obj: ProductSummary,
        tenant: Tenant,
        merchant: AccountSummary? = null
    ) = ProductModel(
        id = obj.id,
        title = obj.title,
        summary = obj.summary,
        price = obj.price?.let { toPriceModel(it, tenant) },
        comparablePrice = toComparablePrice(obj.price, obj.comparablePrice, tenant),
        savings = toSavings(obj.price, obj.comparablePrice, tenant),
        thumbnail = toPictureModel(
            obj = obj.thumbnail,
            defaultPictureUrl = tenant.product.defaultPictureUrl,
            height = THUMBNAIL_HEIGHT
        ),
        merchant = merchant?.let { toAccountModel(merchant) },
        status = obj.status
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
        thumbnail = toPictureModel(
            obj = obj.thumbnail,
            defaultPictureUrl = tenant.product.defaultPictureUrl,
            height = THUMBNAIL_HEIGHT
        ),
        pictures = obj.pictures.map {
            toPictureModel(
                obj = it,
                defaultPictureUrl = tenant.product.defaultPictureUrl,
                height = IMAGE_HEIGHT
            )
        },
        status = obj.status
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
            text = DecimalFormat(tenant.monetaryFormat).format(amount),
            currencySymbol = tenant.currencySymbol,
            numberFormat = tenant.numberFormat
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
                )
        }
        return null
    }

    private fun toPictureModel(
        obj: PictureSummary?,
        defaultPictureUrl: String,
        width: Int? = null,
        height: Int? = null
    ): PictureModel {
        var url = obj?.url?.let {
            imageService.transform(
                url = it,
                transformation = Transformation(
                    dimension = Dimension(width = width, height = height),
                    focus = Focus.AUTO
                )
            )
        }
        if (url.isNullOrEmpty())
            url = defaultPictureUrl

        return PictureModel(url = url)
    }

    open fun toAccountModel(obj: AccountSummary, category: Category? = null) = AccountModel(
        id = obj.id,
        displayName = obj.displayName,
        pictureUrl = obj.pictureUrl,
        business = obj.business,
        retail = obj.retail,
        location = toLocationText(cityService.get(obj.cityId), obj.country),
        businessText = toBusinessText(obj.business, obj.retail),
        category = category?.let { toCategoryModel(it) }
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
        businessText = toBusinessText(obj.business, obj.retail),
        facebookUrl = if (!obj.facebookId.isNullOrEmpty()) "https://www.facebook.com/${obj.facebookId}" else null,
        instagramUrl = if (!obj.instagramId.isNullOrEmpty()) "https://www.instagram.com/${obj.instagramId}" else null,
        twitterUrl = if (!obj.twitterId.isNullOrEmpty()) "https://www.twitter.com/${obj.twitterId}" else null
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
        val locale = LocaleContextHolder.getLocale()
        val timezoneId = currentUser?.timezoneId
        return TransactionModel(
            id = obj.id,
            type = obj.type,
            status = obj.status,
            statusText = getText("shared-ui.transaction.status.${obj.status}"),
            amount = toPriceModel(obj.amount, tenant),
            net = toPriceModel(obj.net, tenant),
            fees = toPriceModel(obj.fees, tenant),
            recipient = accounts[obj.recipientId]?.let { toAccountModel(it) },
            account = accounts[obj.accountId]?.let { toAccountModel(it) },
            paymentMethod = paymentMethod?.let { toPaymentMethodModel(it, tenant, tenantProvider) },
            description = toDescription(obj),
            createdText = DateTimeUtil.convert(obj.created, timezoneId)
                .format(DateTimeFormatter.ofPattern(tenant.dateFormat, locale)),
            currentUserId = currentUser?.id ?: -1,
            orderId = obj.orderId,
            applyFeesToSender = obj.applyFeesToSender
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

    private fun toDescription(obj: TransactionSummary): String =
        getText("shared-ui.transaction.type.${obj.type}")

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
