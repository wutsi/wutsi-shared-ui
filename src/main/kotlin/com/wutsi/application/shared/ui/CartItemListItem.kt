package com.wutsi.application.shared.ui

import com.wutsi.application.shared.Theme
import com.wutsi.application.shared.model.ActionModel
import com.wutsi.application.shared.model.CartItemModel
import com.wutsi.application.shared.service.TransactionUtil.getText
import com.wutsi.flutter.sdui.Button
import com.wutsi.flutter.sdui.Column
import com.wutsi.flutter.sdui.Container
import com.wutsi.flutter.sdui.DropdownButton
import com.wutsi.flutter.sdui.DropdownMenuItem
import com.wutsi.flutter.sdui.Icon
import com.wutsi.flutter.sdui.Image
import com.wutsi.flutter.sdui.ListItem
import com.wutsi.flutter.sdui.Row
import com.wutsi.flutter.sdui.Text
import com.wutsi.flutter.sdui.WidgetAware
import com.wutsi.flutter.sdui.enums.ButtonType
import com.wutsi.flutter.sdui.enums.CrossAxisAlignment
import com.wutsi.flutter.sdui.enums.MainAxisAlignment
import com.wutsi.flutter.sdui.enums.MainAxisSize
import com.wutsi.flutter.sdui.enums.TextDecoration

class CartItemListItem(
    private val model: CartItemModel,
    private val changeQuantityAction: ActionModel,
    private val removeAction: ActionModel,
) : CompositeWidgetAware() {
    override fun toWidgetAware(): WidgetAware = Container(
        child = Column(
            mainAxisAlignment = MainAxisAlignment.start,
            crossAxisAlignment = CrossAxisAlignment.start,
            mainAxisSize = MainAxisSize.min,
            children = listOf(
                ListItem(
                    caption = model.title,
                    subCaption = null,
                    leading = model.thumbnail?.let { Image(url = it.url, width = 48.0, height = 48.8) },
                    trailing = Column(
                        mainAxisAlignment = MainAxisAlignment.start,
                        crossAxisAlignment = CrossAxisAlignment.end,
                        children = listOfNotNull(
                            Text(
                                caption = model.price.text,
                                bold = true
                            ),
                            model.comparablePrice?.let {
                                Text(
                                    model.comparablePrice.text,
                                    size = Theme.TEXT_SIZE_SMALL,
                                    decoration = TextDecoration.Strikethrough
                                )
                            },

                            if (model.quantity > 1)
                                Text(
                                    getText("shared-ui.cart-list-item.price_each", arrayOf(model.unitPrice.text)),
                                    size = Theme.TEXT_SIZE_SMALL,
                                )
                            else
                                null
                        )
                    )
                ),
                Row(
                    mainAxisAlignment = MainAxisAlignment.start,
                    crossAxisAlignment = CrossAxisAlignment.center,
                    children = listOf(
                        Container(padding = 10.0),
                        Container(
                            padding = 10.0,
                            width = 100.0,
                            child = if (model.quantityInStock <= 0)
                                Row(
                                    children = listOf(
                                        Icon(code = Theme.ICON_CANCEL, color = Theme.COLOR_DANGER),
                                        Text(
                                            getText("shared-ui.cart-item-list.out-of-stock"),
                                            color = Theme.COLOR_DANGER,
                                            size = Theme.TEXT_SIZE_SMALL
                                        )
                                    )
                                )
                            else
                                DropdownButton(
                                    name = "quantity",
                                    value = model.quantity.toString(),
                                    children = IntRange(1, model.maxQuantity).map {
                                        DropdownMenuItem(caption = it.toString(), value = it.toString())
                                    },
                                    outlinedBorder = false,
                                    action = changeQuantityAction.action
                                )
                        ),
                        Button(
                            padding = 10.0,
                            stretched = false,
                            type = ButtonType.Text,
                            caption = removeAction.caption,
                            action = removeAction.action
                        )
                    )
                )
            )
        )
    )
}
