package com.wutsi.application.shared.service

import com.wutsi.platform.account.dto.Account

open class TogglesProvider(
    private val toggles: Toggles,
    private val securityContext: SecurityContext,
) {
    open fun isBusinessAccountEnabled(): Boolean =
        toggles.business

    open fun isPaymentEnabled(account: Account): Boolean =
        account.business && (toggles.payment || isTester(account.id))

    open fun isScanEnabled(): Boolean =
        toggles.scan || isTester()

    open fun isSendSmsEnabled(phoneNumber: String): Boolean =
        toggles.sendSmsCode && !isTestTestPhoneNumber(phoneNumber)

    open fun isVerifySmsCodeEnabled(phoneNumber: String): Boolean =
        toggles.verifySmsCode && !isTestTestPhoneNumber(phoneNumber)

    open fun isAccountEnabled(): Boolean =
        toggles.account

    open fun isLogoutEnabled(): Boolean =
        toggles.logout || isTester()

    private fun isTester(): Boolean =
        isTester(securityContext.currentUserId())

    private fun isTester(userId: Long?): Boolean =
        userId != null && toggles.testerUserIds.contains(userId)

    private fun isTestTestPhoneNumber(phoneNumber: String): Boolean =
        toggles.testPhoneNumbers.contains(phoneNumber)
}
