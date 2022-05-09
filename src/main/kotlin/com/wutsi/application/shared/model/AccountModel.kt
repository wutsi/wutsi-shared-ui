package com.wutsi.application.shared.model

data class AccountModel(
    val id: Long = -1,
    val displayName: String? = null,
    val pictureUrl: String? = null,
    val website: String? = null,
    val phoneNumber: String? = null,
    val category: CategoryModel? = null,
    val location: String? = null,
    val biography: String? = null,
    val retail: Boolean = false,
    val business: Boolean = false,
    val businessText: String? = null,
    val facebookUrl: String? = null,
    val twitterUrl: String? = null,
    val instagramUrl: String? = null
)
