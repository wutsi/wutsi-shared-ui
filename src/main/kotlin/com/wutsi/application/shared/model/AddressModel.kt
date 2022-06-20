package com.wutsi.application.shared.model

import com.wutsi.ecommerce.order.entity.AddressType

data class AddressModel(
    val fullName: String = "",
    val street: String? = null,
    val location: String = "",
    val zipCode: String? = null,
    val email: String? = null,
    val type: AddressType = AddressType.POSTAL
)
