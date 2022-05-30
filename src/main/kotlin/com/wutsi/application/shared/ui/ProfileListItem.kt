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
    private val action: Action? = null,
    private val showAccountType: Boolean = true,
    private val showLocation: Boolean = true
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

    private fun toSubCaption(): String? {
        val buff = StringBuilder()
        if (showAccountType && model.business)
            buff.append(TranslationUtil.getText("shared-ui.account.business"))

        if (model.category != null) {
            if (buff.isNotEmpty())
                buff.append(" - ")
            buff.append(model.category.title)
        }

        if (showLocation && !model.location.isNullOrEmpty()) {
            if (buff.isNotEmpty())
                buff.append(" - ")
            buff.append(model.location)
        }

        return if (buff.isEmpty()) null else buff.toString()
    }
}
