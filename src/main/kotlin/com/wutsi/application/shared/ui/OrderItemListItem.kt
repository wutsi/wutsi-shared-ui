package com.wutsi.application.shared.ui

import com.wutsi.application.shared.Theme
import com.wutsi.application.shared.model.OrderItemModel
import com.wutsi.application.shared.service.TranslationUtil.getText
import com.wutsi.flutter.sdui.Column
import com.wutsi.flutter.sdui.Image
import com.wutsi.flutter.sdui.ListItem
import com.wutsi.flutter.sdui.Text
import com.wutsi.flutter.sdui.WidgetAware
import com.wutsi.flutter.sdui.enums.CrossAxisAlignment
import com.wutsi.flutter.sdui.enums.MainAxisAlignment
import com.wutsi.flutter.sdui.enums.TextDecoration

class OrderItemListItem(
    private val model: OrderItemModel,
) : CompositeWidgetAware() {
    override fun toWidgetAware(): WidgetAware = ListItem(
        caption = model.title,
        subCaption = getText("share-ui.order-list-item.quantity", arrayOf(model.quantity.toString())),
        leading = model.thumbnail?.let { Image(url = it.url, width = 48.0, height = 48.8) },
        trailing = Column(
            mainAxisAlignment = MainAxisAlignment.start,
            crossAxisAlignment = CrossAxisAlignment.end,
            children = listOfNotNull(
                model.price?.let { Text(it.text, bold = true) },
                model.comparablePrice?.let {
                    Text(
                        it.text,
                        size = Theme.TEXT_SIZE_SMALL,
                        decoration = TextDecoration.Strikethrough
                    )
                },
                if (model.quantity > 1)
                    model.unitPrice?.let {
                        Text(
                            getText("share-ui.order-list-item.price_each", arrayOf(it.text)),
                            size = Theme.TEXT_SIZE_SMALL,
                        )
                    }
                else
                    null
            )
        )
    )
}
