package com.wutsi.application.shared.service

import com.wutsi.platform.tenant.entity.ToggleName

open class TogglesProvider(
    private val tenantProvider: TenantProvider,
) {
    open fun isAccountEnabled(): Boolean =
        isToggleEnabled(ToggleName.ACCOUNT)

    open fun isMobileMoneyEnabled(): Boolean =
        isToggleEnabled(ToggleName.ACCOUNT_MOBILE_MONEY)

    open fun isBankEnabled(): Boolean =
        isToggleEnabled(ToggleName.ACCOUNT_BANK)

    open fun isContactEnabled(): Boolean =
        isToggleEnabled(ToggleName.CONTACT)

    open fun isCartEnabled(): Boolean =
        isToggleEnabled(ToggleName.CART)

    open fun isDigitalProductEnabled(): Boolean =
        isToggleEnabled(ToggleName.STORE_DIGITAL_PRODUCT)

    open fun isOrderEnabled(): Boolean =
        isToggleEnabled(ToggleName.ORDER)

    open fun isOrderPaymentEnabled(): Boolean =
        isToggleEnabled(ToggleName.ORDER_PAYMENT)

    open fun isPaymentEnabled(): Boolean =
        isToggleEnabled(ToggleName.PAYMENT)

    open fun isScanEnabled(): Boolean =
        isToggleEnabled(ToggleName.SCAN)

    open fun isSendEnabled(): Boolean =
        isToggleEnabled(ToggleName.SEND)

    open fun isShippingEnabled(): Boolean =
        isToggleEnabled(ToggleName.SHIPPING)

    open fun isShippingInStorePickup(): Boolean =
        isToggleEnabled(ToggleName.SHIPPING_IN_STORE_PICKUP)

    open fun isShippingLocalPickupEnabled(): Boolean =
        isToggleEnabled(ToggleName.SHIPPING_LOCAL_PICKUP)

    open fun isShippingLocalDeliveryEnabled(): Boolean =
        isToggleEnabled(ToggleName.SHIPPING_LOCAL_DELIVERY)

    open fun isShippingInternationalDeliveryEnabled(): Boolean =
        isToggleEnabled(ToggleName.SHIPPING_INTERNATIONAL_DELIVERY)

    open fun isShippingEmailDeliveryEnabled(): Boolean =
        isToggleEnabled(ToggleName.SHIPPING_EMAIL_DELIVERY)

    open fun isStoreEnabled(): Boolean =
        isToggleEnabled(ToggleName.STORE)

    open fun isSwitchEnvironmentEnabled(): Boolean =
        isToggleEnabled(ToggleName.SWITCH_ENVIRONMENT)

    fun isToggleEnabled(toggle: ToggleName): Boolean =
        tenantProvider.get().toggles.find { it.name.equals(toggle.name, true) } != null
}
