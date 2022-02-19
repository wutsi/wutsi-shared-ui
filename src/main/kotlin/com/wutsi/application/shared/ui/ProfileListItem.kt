package com.wutsi.application.shared.ui

import com.wutsi.application.shared.Theme
import com.wutsi.application.shared.model.AccountModel
import com.wutsi.application.shared.service.StringUtil
import com.wutsi.flutter.sdui.Action
import com.wutsi.flutter.sdui.ListItem
import com.wutsi.flutter.sdui.WidgetAware

class ProfileListItem(
    private val model: AccountModel,
    private val action: Action? = null
) : CompositeWidgetAware() {
    override fun toWidgetAware(): WidgetAware = ListItem(
        caption = StringUtil.capitalizeFirstLetter(model.displayName),
        subCaption = model.category?.title,
        iconRight = if (action == null) null else Theme.ICON_CHEVRON_RIGHT,
        padding = 10.0,
        leading = Avatar(
            radius = 24.0,
            model = model
        ),
        action = action
    )
}
