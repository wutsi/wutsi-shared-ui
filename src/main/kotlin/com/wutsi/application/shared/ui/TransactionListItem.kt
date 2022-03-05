package com.wutsi.application.shared.ui

import com.wutsi.application.shared.Theme
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

class TransactionListItem(
    private val model: TransactionModel,
    private val action: Action? = null
) : CompositeWidgetAware() {
    override fun toWidgetAware(): WidgetAware = ListItem(
        leading = icon(),
        trailing = amount(),
        caption = model.description ?: "",
        subCaption = getSubCaption(),
        action = action
    )

    private fun icon(): WidgetAware? {
        if (model.type == "CASHIN" || model.type == "CASHOUT")
            return Image(
                width = 48.0,
                height = 48.0,
                url = model.paymentMethod?.iconUrl ?: ""
            )
        else {
            val account = if (model.account?.id == model.currentUserId)
                model.recipient
            else
                model.account

            return account?.let { Avatar(radius = 24.0, model = it) }
        }
    }

    private fun amount(): WidgetAware {
        val children = mutableListOf(
            Text(
                caption = if (model.recipient?.id == model.currentUserId)
                    model.netText
                else
                    model.amountText,
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
        if (model.status != "SUCCESSFUL")
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

    private fun getColor(): String =
        when (model.status.uppercase()) {
            "FAILED" -> Theme.COLOR_DANGER
            "PENDING" -> Theme.COLOR_WARNING
            else -> when (model.type.uppercase()) {
                "CASHIN" -> Theme.COLOR_SUCCESS
                "CASHOUT" -> Theme.COLOR_DANGER
                else -> if (model.recipient?.id == model.currentUserId)
                    Theme.COLOR_SUCCESS
                else
                    Theme.COLOR_DANGER
            }
        }

    private fun getSubCaption(): String? {
        if (model.type == "CASHIN" || model.type == "CASHOUT") {
            return model.paymentMethod?.phoneNumber
        } else {
            return if (model.account?.id == model.currentUserId)
                model.recipient?.displayName
            else
                model.account?.displayName
        }
    }
}
