package com.wutsi.application.shared.ui

import com.wutsi.application.shared.service.StringUtil
import com.wutsi.flutter.sdui.CircleAvatar
import com.wutsi.flutter.sdui.Image
import com.wutsi.flutter.sdui.Text
import com.wutsi.flutter.sdui.WidgetAware

class Avatar(
    private val radius: Double,
    private val pictureUrl: String? = null,
    private val text: String? = null,
    private val textSize: Double? = null
) : CompositeWidgetAware() {
    override fun toWidgetAware(): WidgetAware =
        CircleAvatar(
            radius = radius,
            child = if (pictureUrl.isNullOrEmpty())
                Text(StringUtil.initials(text), bold = true, size = textSize)
            else
                Image(pictureUrl)
        )
}
