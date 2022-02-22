package com.wutsi.application.shared.model

data class OrderItemModel(
    val title: String = "",
    val unitPrice: PriceModel? = null,
    val price: PriceModel? = null,
    val comparablePrice: PriceModel? = null,
    val thumbnail: PictureModel? = null,
    val savings: SavingsModel? = null,
    val quantity: Int = 1
)
