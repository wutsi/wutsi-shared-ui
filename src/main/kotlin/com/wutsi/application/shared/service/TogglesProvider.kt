package com.wutsi.application.shared.service

import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles

open class TogglesProvider(
    private val toggles: Toggles,
    private val securityContext: SecurityContext,
    private val env: Environment
) {
    open fun isAccountEnabled(): Boolean =
        toggles.account

    open fun isBusinessAccountEnabled(): Boolean =
        toggles.business

    open fun isCartEnabled(): Boolean =
        toggles.cart

    open fun isContactEnabled(): Boolean =
        toggles.contact

    open fun isDigitalProductEnabled(): Boolean =
        toggles.digitalProduct

    open fun isFeedbackEnabled(): Boolean =
        toggles.feedback

    open fun isLogoutEnabled(): Boolean =
        toggles.logout

    open fun isOrderEnabled(): Boolean =
        toggles.order

    open fun isPaymentEnabled(): Boolean =
        toggles.payment

    open fun isScanEnabled(): Boolean =
        toggles.scan

    open fun isSendSmsCodeEnabled(phoneNumber: String): Boolean =
        toggles.sendSmsCode && !isTestPhoneNumber(phoneNumber)

    open fun isShippingInternationalEnabled(): Boolean =
        toggles.shippingInternational

    open fun isStoreEnabled(): Boolean =
        toggles.store

    open fun isSwitchEnvironmentEnabled(): Boolean =
        toggles.switchEnvironment || isTester()

    open fun isVerifySmsCodeEnabled(phoneNumber: String): Boolean =
        toggles.verifySmsCode && !isTestPhoneNumber(phoneNumber)

    private fun isTester(): Boolean =
        isTester(securityContext.currentAccountId())

    private fun isTester(userId: Long?): Boolean =
        userId != null && toggles.testerUserIds.contains(userId)

    private fun isTestPhoneNumber(phoneNumber: String): Boolean =
        toggles.testPhoneNumbers.contains(phoneNumber)

    private fun isNotProd(): Boolean =
        !isProd()

    private fun isProd(): Boolean =
        env.acceptsProfiles(Profiles.of("prod"))
}
