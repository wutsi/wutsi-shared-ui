package com.wutsi.application.shared.ui

import com.wutsi.application.shared.Theme
import com.wutsi.application.shared.model.AccountModel
import com.wutsi.application.shared.service.StringUtil
import com.wutsi.application.shared.service.TranslationUtil
import com.wutsi.flutter.sdui.Action
import com.wutsi.flutter.sdui.Icon
import com.wutsi.flutter.sdui.ListItem
import com.wutsi.flutter.sdui.WidgetAware

class ProfileListItem(
    private val model: AccountModel,
    private val action: Action? = null
) : CompositeWidgetAware() {
    override fun toWidgetAware(): WidgetAware = ListItem(
        caption = StringUtil.capitalizeFirstLetter(model.displayName),
        subCaption = toSubCaption(),
        iconRight = if (action == null) null else Theme.ICON_CHEVRON_RIGHT,
        padding = 10.0,
        leading = Avatar(
            radius = 24.0,
            model = model
        ),
        action = action,
        trailing = action?.let { Icon(code = Theme.ICON_CHEVRON_RIGHT) }
    )

    private fun toSubCaption(): String? =
        if (model.business == null)
            null
        else
            TranslationUtil.getText("shared-ui.account.business") +
                (model.category?.let { " - ${it.title}" } ?: "")
}
