package com.wutsi.application.shared.ui

import com.wutsi.application.shared.Theme
import com.wutsi.flutter.sdui.Action
import com.wutsi.flutter.sdui.CircleAvatar
import com.wutsi.flutter.sdui.Container
import com.wutsi.flutter.sdui.IconButton
import com.wutsi.flutter.sdui.WidgetAware

class TitleBarAction(
    private val icon: String,
    private val action: Action
) : CompositeWidgetAware() {
    override fun toWidgetAware(): WidgetAware =
        Container(
            padding = 4.0,
            child = CircleAvatar(
                radius = 20.0,
                backgroundColor = Theme.COLOR_PRIMARY_LIGHT,
                child = IconButton(
                    icon = icon,
                    size = 20.0,
                    action = action
                )
            )
        )
}
