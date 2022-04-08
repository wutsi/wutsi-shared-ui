package com.wutsi.application.shared.service

open class Toggles {
    var account: Boolean = false
    var business: Boolean = false
    var cart: Boolean = false
    var contact: Boolean = false
    var digitalProduct: Boolean = false
    var feedback: Boolean = false
    var logout: Boolean = false
    var order: Boolean = false
    var payment: Boolean = false
    var scan: Boolean = false
    var sendSmsCode: Boolean = false
    var shippingInternational: Boolean = false
    var store: Boolean = false
    var switchEnvironment: Boolean = false
    var verifySmsCode: Boolean = false

    var testerUserIds: List<Long> = emptyList()
    var testPhoneNumbers: List<String> = emptyList()
}
