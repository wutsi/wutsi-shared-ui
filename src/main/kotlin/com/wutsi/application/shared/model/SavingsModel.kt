package com.wutsi.application.shared.model

data class SavingsModel(
    val value: Double = 0.0,
    val percent: Int? = null,
    val text: String? = null,
    val percentText: String? = null
)
