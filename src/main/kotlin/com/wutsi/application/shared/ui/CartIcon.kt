package com.wutsi.application.shared.ui

import com.wutsi.application.shared.Theme
import com.wutsi.flutter.sdui.Action
import com.wutsi.flutter.sdui.Badge
import com.wutsi.flutter.sdui.IconButton
import com.wutsi.flutter.sdui.Positioned
import com.wutsi.flutter.sdui.Stack
import com.wutsi.flutter.sdui.WidgetAware
import kotlin.math.max

class CartIcon(
    private val productCount: Int,
    private val size: Double = 20.0,
    private val action: Action? = null
) : CompositeWidgetAware() {
    override fun toWidgetAware(): WidgetAware = Stack(
        children = listOfNotNull(
            IconButton(
                icon = Theme.ICON_CART,
                size = size,
                action = action
            ),
            if (productCount > 0)
                Positioned(
                    top = 6.0,
                    right = 6.0,
                    child = Badge(
                        padding = 3.0,
                        fontSize = max(10.0, size / 2),
                        caption = productCount.toString()
                    )
                )
            else
                null
        )
    )
}
