package com.wutsi.application.shared.service

import com.wutsi.platform.tenant.entity.ToggleName

open class TogglesProvider(
    private val tenantProvider: TenantProvider,
) {
    private fun isToggleEnabled(toggle: ToggleName): Boolean =
        tenantProvider.get().toggles.find { it.name.equals(toggle.name, true) } != null

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

    open fun isPaymentEnabled(): Boolean =
        isToggleEnabled(ToggleName.PAYMENT)

    open fun isScanEnabled(): Boolean =
        isToggleEnabled(ToggleName.SCAN)

    open fun isShippingEnabled(): Boolean =
        isToggleEnabled(ToggleName.SHIPPING)

    open fun isShippingInternationalEnabled(): Boolean =
        isToggleEnabled(ToggleName.SHIPPING_INTERNATIONAL)

    open fun isStoreEnabled(): Boolean =
        isToggleEnabled(ToggleName.STORE)

    open fun isSwitchEnvironmentEnabled(): Boolean =
        isToggleEnabled(ToggleName.SWITCH_ENVIRONMENT)
}
