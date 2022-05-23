package com.wutsi.application.shared.ui

import com.wutsi.application.shared.Theme
import com.wutsi.application.shared.model.ProductModel
import com.wutsi.application.shared.service.StringUtil
import com.wutsi.application.shared.service.TranslationUtil.getText
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
    private val action: Action? = null,
    private val showStatus: Boolean = false,
) : CompositeWidgetAware() {
    override fun toWidgetAware(): WidgetAware = ListItem(
        caption = StringUtil.capitalizeFirstLetter(model.title),
        subCaption = if (showStatus) getText("product.status.${model.status}") else null,
        padding = 10.0,
        leading = model.thumbnail?.let { Image(it.url, width = 48.0, height = 48.0) },
        trailing = if (model.price != null)
            Column(
                mainAxisAlignment = MainAxisAlignment.start,
                crossAxisAlignment = CrossAxisAlignment.end,
                children = listOfNotNull(
                    Text(
                        caption = model.price.text,
                        bold = true,
                        color = Theme.COLOR_PRIMARY,
                    ),
                    if (model.comparablePrice != null)
                        Text(
                            caption = model.comparablePrice.text,
                            color = Theme.COLOR_GRAY,
                            decoration = TextDecoration.Strikethrough,
                            size = Theme.TEXT_SIZE_SMALL,
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
