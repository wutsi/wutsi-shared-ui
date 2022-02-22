package com.wutsi.application.shared.model

data class CartItemModel(
    val title: String,
    val unitPrice: PriceModel,
    val price: PriceModel,
    val comparablePrice: PriceModel?,
    val thumbnail: PictureModel?,
    val savings: SavingsModel?,
    val quantity: Int,
    val quantityInStock: Int,
    val maxQuantity: Int
)
