package com.wutsi.application.shared.ui

import com.wutsi.application.shared.Theme
import com.wutsi.flutter.sdui.Action
import com.wutsi.flutter.sdui.CircleAvatar
import com.wutsi.flutter.sdui.Container
import com.wutsi.flutter.sdui.WidgetAware

class TitleBarCartAction(
    private val productCount: Int,
    private val action: Action
) : CompositeWidgetAware() {
    override fun toWidgetAware(): WidgetAware =
        Container(
            padding = 4.0,
            child = CircleAvatar(
                radius = 20.0,
                backgroundColor = Theme.COLOR_PRIMARY_LIGHT,
                child = CartIcon(
                    productCount = productCount,
                    size = 20.0,
                    action = action
                ),
            )
        )
}
