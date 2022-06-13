package com.wutsi.application.shared.ui

import com.wutsi.application.shared.Theme
import com.wutsi.application.shared.model.PriceModel
import com.wutsi.application.shared.model.TransactionModel
import com.wutsi.flutter.sdui.Action
import com.wutsi.flutter.sdui.Column
import com.wutsi.flutter.sdui.Image
import com.wutsi.flutter.sdui.ListItem
import com.wutsi.flutter.sdui.Text
import com.wutsi.flutter.sdui.WidgetAware
import com.wutsi.flutter.sdui.enums.CrossAxisAlignment
import com.wutsi.flutter.sdui.enums.MainAxisAlignment
import com.wutsi.flutter.sdui.enums.TextAlignment
import com.wutsi.platform.payment.core.Status
import com.wutsi.platform.payment.entity.TransactionType

class TransactionListItem(
    private val model: TransactionModel,
    private val action: Action? = null
) : CompositeWidgetAware() {
    override fun toWidgetAware(): WidgetAware = ListItem(
        leading = toIconWidget(),
        trailing = toAmountWidget(),
        caption = model.description ?: "",
        subCaption = getSubCaption(),
        action = action
    )

    private fun isCashIn() = model.type == TransactionType.CASHIN.name

    private fun isCashOut() = model.type == TransactionType.CASHOUT.name

    private fun isCharge() = model.type == TransactionType.CHARGE.name

    private fun isTransfer() = model.type == TransactionType.TRANSFER.name

    private fun isSender() = model.account?.id == model.currentUserId

    private fun isRecipient() = model.recipient?.id == model.currentUserId

    private fun toIconWidget(): WidgetAware? {
        if (isCashIn() || isCashOut())
            return Image(
                width = 48.0,
                height = 48.0,
                url = model.paymentMethod?.iconUrl ?: ""
            )
        else {
            val account = if (isSender())
                model.recipient
            else
                model.account

            return account?.let { Avatar(radius = 24.0, model = it) }
        }
    }

    private fun toAmountWidget(): WidgetAware {
        val children = mutableListOf(
            Text(
                caption = toAmountSign() + toAmount().text,
                bold = true,
                color = getColor(),
                alignment = TextAlignment.Right
            ),
            Text(
                caption = model.createdText,
                size = Theme.TEXT_SIZE_SMALL,
                alignment = TextAlignment.Right
            ),
        )
        if (model.status != Status.SUCCESSFUL.name)
            children.add(
                Text(
                    caption = model.statusText,
                    bold = true,
                    color = getColor(),
                    size = Theme.TEXT_SIZE_SMALL
                )
            )

        return Column(
            mainAxisAlignment = MainAxisAlignment.spaceAround,
            crossAxisAlignment = CrossAxisAlignment.end,
            children = children,
        )
    }

    private fun toAmount(): PriceModel =
        if (isSender() || !model.applyFeesToSender)
            model.amount
        else
            model.net

    private fun toAmountSign(): String =
        if (isCashOut() ||
            (isCharge() && isSender()) ||
            (isTransfer() && isSender())
        )
            "-"
        else
            ""

    private fun getColor(): String? =
        when (model.status.uppercase()) {
            Status.FAILED.name -> Theme.COLOR_DANGER
            Status.PENDING.name -> Theme.COLOR_WARNING
            else -> null
        }

    private fun getSubCaption(): String? {
        if (isCashIn() || isCashOut()) {
            return model.paymentMethod?.phoneNumber
        } else {
            return if (isSender())
                model.recipient?.displayName
            else
                model.account?.displayName
        }
    }
}
