package com.wutsi.application.shared.model

data class AccountModel(
    val displayName: String? = null,
    val pictureUrl: String? = null,
    val website: String? = null,
    val phoneNumber: String? = null,
    val category: String? = null,
    val location: String? = null,
    val biography: String? = null,
    val retail: Boolean = false,
    val business: Boolean = false,
)
