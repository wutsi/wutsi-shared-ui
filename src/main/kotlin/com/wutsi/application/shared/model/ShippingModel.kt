package com.wutsi.application.shared.model

data class ShippingModel(
    val type: String = "",
    val expectedDelivered: String? = null,
    val message: String? = null,
    val rate: String? = null
)
