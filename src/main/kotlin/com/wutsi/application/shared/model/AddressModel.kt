package com.wutsi.application.shared.model

data class AddressModel(
    val fullName: String = "",
    val street: String? = null,
    val location: String = "",
    val zipCode: String? = null,
    val email: String? = null
)
