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

    open fun isInternationalShippingEnabled(): Boolean =
        isToggleEnabled(ToggleName.SHIPPING_INTERNATIONAL)

    open fun isStoreEnabled(): Boolean =
        isToggleEnabled(ToggleName.STORE)

    open fun isSwitchEnvironmentEnabled(): Boolean =
        isToggleEnabled(ToggleName.SWITCH_ENVIRONMENT)

    private fun isToggleEnabled(toggle: ToggleName): Boolean =
        tenantProvider.get().toggles.find { it.name.equals(toggle.name, true) } != null
}
