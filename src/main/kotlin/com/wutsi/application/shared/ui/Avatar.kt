package com.wutsi.application.shared.ui

import com.wutsi.application.shared.model.AccountModel
import com.wutsi.application.shared.service.StringUtil
import com.wutsi.flutter.sdui.Action
import com.wutsi.flutter.sdui.CircleAvatar
import com.wutsi.flutter.sdui.Image
import com.wutsi.flutter.sdui.Text
import com.wutsi.flutter.sdui.WidgetAware
import kotlin.math.max

class Avatar(
    private val model: AccountModel,
    private val radius: Double,
    private val action: Action? = null
) : CompositeWidgetAware() {
    override fun toWidgetAware(): WidgetAware =
        CircleAvatar(
            action = action,
            radius = radius,
            child = if (model.pictureUrl.isNullOrEmpty())
                Text(StringUtil.initials(model.displayName), bold = true, size = max(14.0, radius - 8.0))
            else
                Image(model.pictureUrl)
        )
}
