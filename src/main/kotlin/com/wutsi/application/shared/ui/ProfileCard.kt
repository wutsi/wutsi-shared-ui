package com.wutsi.application.shared.ui

import com.wutsi.application.shared.Theme
import com.wutsi.application.shared.model.AccountModel
import com.wutsi.application.shared.service.PhoneUtil.format
import com.wutsi.flutter.sdui.Action
import com.wutsi.flutter.sdui.Column
import com.wutsi.flutter.sdui.Container
import com.wutsi.flutter.sdui.Icon
import com.wutsi.flutter.sdui.Row
import com.wutsi.flutter.sdui.Text
import com.wutsi.flutter.sdui.WidgetAware
import com.wutsi.flutter.sdui.Wrap
import com.wutsi.flutter.sdui.enums.ActionType
import com.wutsi.flutter.sdui.enums.Alignment
import com.wutsi.flutter.sdui.enums.Axis
import com.wutsi.flutter.sdui.enums.MainAxisAlignment
import com.wutsi.flutter.sdui.enums.MainAxisSize
import com.wutsi.flutter.sdui.enums.TextAlignment
import com.wutsi.flutter.sdui.enums.TextDecoration

class ProfileCard(
    private val model: AccountModel,
    private val showWebsite: Boolean = true,
    private val showPhoneNumber: Boolean = true,
    private val type: ProfileCardType = ProfileCardType.Full
) : CompositeWidgetAware() {
    override fun toWidgetAware(): WidgetAware {
        var pad = 0
        val children = mutableListOf<WidgetAware>(
            Container(
                padding = if (pad++ % 2 == 0) 10.0 else null,
                alignment = Alignment.Center,
                child = Avatar(
                    radius = 32.0,
                    model = model
                )
            ),
            Container(
                alignment = Alignment.Center,
                padding = if (pad++ % 2 == 0) 10.0 else null,
                child = Row(
                    mainAxisAlignment = MainAxisAlignment.center,
                    children = listOf(
                        Text(
                            caption = model.displayName ?: "",
                            alignment = TextAlignment.Center,
                            size = Theme.TEXT_SIZE_LARGE,
                            color = Theme.COLOR_PRIMARY,
                            bold = true,
                        ),
                        if (model.retail)
                            Icon(
                                Theme.ICON_VERIFIED,
                                color = Theme.COLOR_PRIMARY,
                                size = Theme.TEXT_SIZE_LARGE
                            )
                        else
                            Container(),
                    ),
                )
            ),
        )

        // Phone
        if (showPhoneNumber && !model.phoneNumber.isNullOrEmpty()) {
            children.add(
                Container(
                    padding = if (pad++ % 2 == 0) 10.0 else null,
                    child = Text(
                        caption = format(model.phoneNumber)!!,
                        alignment = TextAlignment.Center,
                        color = Theme.COLOR_GRAY,
                    )
                )
            )
        }

        // Bio
        if (!model.biography.isNullOrEmpty() && type == ProfileCardType.Full)
            children.add(
                Container(
                    padding = if (pad++ % 2 == 0) 10.0 else null,
                    alignment = Alignment.Center,
                    child = Text(
                        alignment = TextAlignment.Center,
                        caption = model.biography
                    )
                )
            )

        // Web site
        if (showWebsite && !model.website.isNullOrEmpty() && type == ProfileCardType.Full)
            children.add(
                Container(
                    padding = if (pad++ % 2 == 0) 10.0 else null,
                    alignment = Alignment.Center,
                    child = Text(
                        caption = sanitizeWebsite(model.website),
                        color = Theme.COLOR_PRIMARY,
                        decoration = TextDecoration.Underline
                    ),
                    action = Action(
                        type = ActionType.Navigate,
                        url = model.website
                    )
                )
            )

        // More
        if (type == ProfileCardType.Full) {
            val more = mutableListOf<WidgetAware>()
            if (!model.location.isNullOrEmpty()) {
                more.add(
                    Container(
                        child = Row(
                            mainAxisSize = MainAxisSize.min,
                            children = listOf(
                                Icon(code = Theme.ICON_LOCATION, size = 16.0),
                                Container(padding = 2.0),
                                Text(
                                    caption = model.location,
                                    color = Theme.COLOR_GRAY,
                                )
                            ),
                        )
                    )
                )
            }
            if (model.business && model.category != null) {
                more.add(
                    Container(
                        child = Row(
                            mainAxisSize = MainAxisSize.min,
                            children = listOf(
                                Icon(code = Theme.ICON_BUSINESS, size = 16.0),
                                Container(padding = 2.0),
                                Text(
                                    caption = model.category,
                                    color = Theme.COLOR_GRAY,
                                )
                            ),
                        )
                    )
                )
            }
            if (more.isNotEmpty())
                children.add(
                    Container(
                        padding = if (pad++ % 2 == 0) 10.0 else null,
                        child = Wrap(
                            children = more,
                            spacing = 10.0,
                            runSpacing = 10.0,
                            direction = Axis.Horizontal
                        )
                    )
                )
        }

        return Column(
            children = children
        )
    }

    private fun sanitizeWebsite(website: String): String {
        val i = website.indexOf("//")
        return if (i > 0)
            website.substring(i + 2)
        else
            website
    }
}
