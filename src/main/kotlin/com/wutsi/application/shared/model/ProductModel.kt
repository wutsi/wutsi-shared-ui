package com.wutsi.application.shared.model

data class ProductModel(
    val id: Long = -1,
    val title: String = "",
    val summary: String? = null,
    val description: String? = null,
    val price: PriceModel? = null,
    val comparablePrice: PriceModel? = null,
    val thumbnail: PictureModel? = null,
    val pictures: List<PictureModel>? = null,
    val savings: SavingsModel? = null,
    val visible: Boolean = true
)
