package com.wutsi.application.shared.model

data class PriceSummaryModel(
    val itemCount: Int,
    val subTotal: PriceModel,
    val total: PriceModel,
    val deliveryFees: PriceModel? = null,
    val savings: SavingsModel? = null,
    val action: ActionModel? = null
)
