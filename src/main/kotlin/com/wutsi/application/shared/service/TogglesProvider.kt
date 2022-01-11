package com.wutsi.application.shared.service

import com.wutsi.platform.account.dto.Account

open class TogglesProvider(
    private val toggles: Toggles,
    private val securityContext: SecurityContext,
) {
    fun isBusinessAccountEnabled(): Boolean =
        toggles.business

    fun isPaymentEnabled(account: Account): Boolean =
        account.business && (toggles.payment || isTester(account.id))

    fun isScanEnabled(): Boolean =
        toggles.scan || isTester()

    fun isSendSmsEnabled(phoneNumber: String): Boolean =
        toggles.sendSmsCode && !isTestTestPhoneNumber(phoneNumber)

    fun isVerifySmsCodeEnabled(phoneNumber: String): Boolean =
        toggles.verifySmsCode && !isTestTestPhoneNumber(phoneNumber)

    fun isAccountEnabled(): Boolean =
        toggles.account

    fun isLogoutEnabled(): Boolean =
        toggles.logout || isTester()

    private fun isTester(): Boolean =
        isTester(securityContext.currentUserId())

    private fun isTester(userId: Long?): Boolean =
        userId != null && toggles.testerUserIds.contains(userId)

    private fun isTestTestPhoneNumber(phoneNumber: String): Boolean =
        toggles.testPhoneNumbers.contains(phoneNumber)
}
