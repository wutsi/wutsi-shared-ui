package com.wutsi.application.shared.model

data class TransactionModel(
    val id: String = "",
    val type: String = "",
    val status: String = "",
    val statusText: String = "",
    val amount: PriceModel = PriceModel(),
    val net: PriceModel = PriceModel(),
    val fees: PriceModel = PriceModel(),
    val account: AccountModel? = null,
    val recipient: AccountModel? = null,
    val paymentMethod: PaymentMethodModel? = null,
    val currentUserId: Long? = null,
    val createdText: String = "",
    val description: String? = null,
    val orderId: String? = null,
    val applyFeesToSender: Boolean = false,
)
