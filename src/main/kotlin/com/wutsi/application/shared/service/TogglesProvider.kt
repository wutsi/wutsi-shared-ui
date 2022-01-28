package com.wutsi.application.shared.service

import com.wutsi.platform.account.dto.Account

open class TogglesProvider(
    private val toggles: Toggles,
    private val securityContext: SecurityContext,
) {
    open fun isAccountEnabled(): Boolean =
        toggles.account

    open fun isBusinessAccountEnabled(): Boolean =
        toggles.business

    open fun isContactEnabled(): Boolean =
        toggles.contact

    open fun isCatalogEnabled(): Boolean =
        (toggles.catalog && isBusinessAccountEnabled()) || isDeveloper()

    open fun isFeedbackEnabled(): Boolean =
        toggles.feedback

    open fun isPaymentEnabled(account: Account): Boolean =
        account.business && (toggles.payment || isTester(account.id))

    open fun isScanEnabled(): Boolean =
        toggles.scan || isTester()

    open fun isSendSmsCodeEnabled(phoneNumber: String): Boolean =
        toggles.sendSmsCode && !isTestPhoneNumber(phoneNumber)

    open fun isVerifySmsCodeEnabled(phoneNumber: String): Boolean =
        toggles.verifySmsCode && !isTestPhoneNumber(phoneNumber)

    open fun isLogoutEnabled(): Boolean =
        toggles.logout || isTester()

    private fun isDeveloper(): Boolean =
        isDeveloper(securityContext.currentAccountId())

    private fun isDeveloper(userId: Long?): Boolean =
        userId != null && toggles.devUserIds.contains(userId)

    private fun isTester(): Boolean =
        isTester(securityContext.currentAccountId())

    private fun isTester(userId: Long?): Boolean =
        userId != null && toggles.testerUserIds.contains(userId)

    private fun isTestPhoneNumber(phoneNumber: String): Boolean =
        toggles.testPhoneNumbers.contains(phoneNumber)
}
