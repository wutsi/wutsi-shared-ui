package com.wutsi.application.shared.service

open class Toggles {
    var sendSmsCode: Boolean = true
    var verifySmsCode: Boolean = true
    var payment: Boolean = true
    var scan: Boolean = true
    var account: Boolean = true
    var business: Boolean = true
    var logout: Boolean = true
    var testerUserIds: List<Long> = emptyList()
    var testPhoneNumbers: List<String> = emptyList()
}
