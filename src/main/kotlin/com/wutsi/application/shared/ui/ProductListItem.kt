package com.wutsi.application.shared.ui

import com.wutsi.application.shared.Theme
import com.wutsi.application.shared.model.ProductModel
import com.wutsi.application.shared.service.StringUtil
import com.wutsi.flutter.sdui.Action
import com.wutsi.flutter.sdui.Column
import com.wutsi.flutter.sdui.Image
import com.wutsi.flutter.sdui.ListItem
import com.wutsi.flutter.sdui.Text
import com.wutsi.flutter.sdui.WidgetAware
import com.wutsi.flutter.sdui.enums.CrossAxisAlignment
import com.wutsi.flutter.sdui.enums.MainAxisAlignment
import com.wutsi.flutter.sdui.enums.TextDecoration

class ProductListItem(
    private val model: ProductModel,
    private val action: Action? = null
) : CompositeWidgetAware() {
    override fun toWidgetAware(): WidgetAware = ListItem(
        caption = StringUtil.capitalizeFirstLetter(model.title),
        padding = 10.0,
        leading = model.thumbnail?.let { Image(it.url, width = 48.0, height = 48.0) },
        trailing = if (model.price != null)
            Column(
                mainAxisAlignment = MainAxisAlignment.start,
                crossAxisAlignment = CrossAxisAlignment.start,
                children = listOfNotNull(
                    Text(
                        caption = model.price.text,
                        bold = true,
                        color = Theme.COLOR_PRIMARY,
                    ),
                    if (model.comparablePrice != null)
                        Text(
                            caption = model.comparablePrice.text,
                            bold = true,
                            decoration = TextDecoration.Strikethrough
                        )
                    else
                        null,
                ),
            )
        else
            null,
        action = action,
    )
}
