package com.wutsi.application.shared.ui

import com.wutsi.application.shared.Theme
import com.wutsi.application.shared.model.ProductModel
import com.wutsi.application.shared.service.StringUtil
import com.wutsi.flutter.sdui.Action
import com.wutsi.flutter.sdui.Icon
import com.wutsi.flutter.sdui.Image
import com.wutsi.flutter.sdui.ListItem
import com.wutsi.flutter.sdui.WidgetAware

class ProductListItem(
    private val model: ProductModel,
    private val action: Action? = null
) : CompositeWidgetAware() {
    override fun toWidgetAware(): WidgetAware = ListItem(
        caption = StringUtil.capitalizeFirstLetter(model.title),
        subCaption = model.price?.text,
        padding = 10.0,
        leading = model.thumbnail?.let { Image(it.url, width = 48.0, height = 48.0) },
        trailing = if (action == null) null else Icon(Theme.ICON_CHEVRON_RIGHT, size = 24.0),
        action = action,
    )
}
