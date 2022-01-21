package com.wutsi.application.shared.entity

data class CityEntity(
    val id: Long,
    val name: String = "",
    val asciiName: String = "",
    val country: String
)
