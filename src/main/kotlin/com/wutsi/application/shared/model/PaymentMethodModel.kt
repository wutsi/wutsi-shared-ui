package com.wutsi.application.shared.model

data class PaymentMethodModel(
    val token: String? = null,
    val phoneNumber: String? = null,
    val iconUrl: String,
)
