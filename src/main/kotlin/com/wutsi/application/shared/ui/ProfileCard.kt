package com.wutsi.application.shared.ui

import com.wutsi.application.shared.Theme
import com.wutsi.application.shared.model.AccountModel
import com.wutsi.application.shared.service.PhoneUtil.format
import com.wutsi.application.shared.service.TranslationUtil.getText
import com.wutsi.flutter.sdui.Action
import com.wutsi.flutter.sdui.Center
import com.wutsi.flutter.sdui.Column
import com.wutsi.flutter.sdui.Container
import com.wutsi.flutter.sdui.Icon
import com.wutsi.flutter.sdui.Image
import com.wutsi.flutter.sdui.Row
import com.wutsi.flutter.sdui.Text
import com.wutsi.flutter.sdui.WidgetAware
import com.wutsi.flutter.sdui.Wrap
import com.wutsi.flutter.sdui.enums.ActionType
import com.wutsi.flutter.sdui.enums.Alignment
import com.wutsi.flutter.sdui.enums.Axis
import com.wutsi.flutter.sdui.enums.CrossAxisAlignment
import com.wutsi.flutter.sdui.enums.MainAxisAlignment
import com.wutsi.flutter.sdui.enums.MainAxisSize
import com.wutsi.flutter.sdui.enums.TextAlignment
import com.wutsi.flutter.sdui.enums.TextDecoration

class ProfileCard(
    private val model: AccountModel,
    private val showWebsite: Boolean = true,
    private val showPhoneNumber: Boolean = true,
    private val type: ProfileCardType = ProfileCardType.FULL,
    private val assetUrl: String? = null,
    private val action: Action? = null
) : CompositeWidgetAware() {
    override fun toWidgetAware(): WidgetAware {
        var pad = 0
        val children = mutableListOf<WidgetAware>(
            Container(
                padding = if (pad++ % 2 == 0) 10.0 else null,
                alignment = Alignment.Center,
                child = Avatar(
                    radius = 32.0,
                    model = model,
                    action = action
                )
            ),
            Container(
                alignment = Alignment.Center,
                padding = if (pad++ % 2 == 0) 10.0 else null,
                child = Text(
                    caption = model.displayName ?: "",
                    alignment = TextAlignment.Center,
                    size = Theme.TEXT_SIZE_LARGE,
                    color = Theme.COLOR_PRIMARY,
                    bold = true,
                ),
                action = action
            )
        )

        // Account Type
        if (model.business)
            children.add(
                Container(
                    padding = if (pad++ % 2 == 0) 10.0 else null,
                    alignment = Alignment.Center,
                    child = Text(
                        caption = getText("shared-ui.account.business"),
                        alignment = TextAlignment.Center,
                        color = Theme.COLOR_GRAY,
                    )
                )
            )

        // Phone
        if (showPhoneNumber && !model.phoneNumber.isNullOrEmpty()) {
            children.add(
                Container(
                    padding = if (pad++ % 2 == 0) 10.0 else null,
                    alignment = Alignment.Center,
                    child = Text(
                        caption = format(model.phoneNumber)!!,
                        alignment = TextAlignment.Center,
                        bold = true
                    )
                )
            )
        }

        // Bio
        if (model.business && !model.biography.isNullOrEmpty() && type == ProfileCardType.FULL)
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

        // Location + Category + Website
        if (type == ProfileCardType.FULL) {
            val more = mutableListOf<WidgetAware>()

            // Location
            if (!model.location.isNullOrEmpty())
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

            // Category
            if (model.business && model.category != null)
                more.add(
                    Container(
                        child = Row(
                            mainAxisSize = MainAxisSize.min,
                            children = listOf(
                                Icon(code = Theme.ICON_BUSINESS, size = 16.0),
                                Container(padding = 2.0),
                                Text(
                                    caption = model.category.title,
                                    color = Theme.COLOR_GRAY,
                                )
                            ),
                        )
                    )
                )

            // Website
            if (model.business && showWebsite && !model.website.isNullOrEmpty())
                more.add(
                    Container(
                        padding = if (pad++ % 2 == 0) 10.0 else null,
                        alignment = Alignment.Center,
                        child = Row(
                            mainAxisSize = MainAxisSize.min,
                            children = listOf(
                                Icon(code = Theme.ICON_LINK, size = 16.0),
                                Container(padding = 2.0),
                                Text(
                                    caption = sanitizeWebsite(model.website),
                                    color = Theme.COLOR_PRIMARY,
                                    decoration = TextDecoration.Underline
                                )
                            )
                        ),
                        action = Action(
                            type = ActionType.Navigate,
                            url = model.website
                        )
                    )
                )

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

        // Social Icons
        if (type == ProfileCardType.FULL && model.business && assetUrl != null) {
            val socialIcons = mutableListOf<WidgetAware>()
            model.facebookUrl?.let {
                socialIcons.add(toSocialIcon("$assetUrl/assets/images/social/facebook.png", it))
            }
            model.instagramUrl?.let {
                socialIcons.add(toSocialIcon("$assetUrl/assets/images/social/instagram.png", it))
            }
            model.twitterUrl?.let {
                socialIcons.add(toSocialIcon("$assetUrl/assets/images/social/twitter.png", it))
            }
            if (socialIcons.isNotEmpty())
                children.add(
                    Container(
                        padding = if (pad++ % 2 == 0) 10.0 else null,
                        child = Row(
                            mainAxisAlignment = MainAxisAlignment.spaceAround,
                            crossAxisAlignment = CrossAxisAlignment.center,
                            children = socialIcons
                        )
                    )
                )
        }

        return Center(
            child = Column(
                children = children,
                mainAxisAlignment = MainAxisAlignment.center,
                crossAxisAlignment = CrossAxisAlignment.center
            )
        )
    }

    private fun toSocialIcon(url: String, navigationUrl: String) = Container(
        padding = 10.0,
        child = Image(
            width = 32.0,
            height = 32.0,
            url = url,
        ),
        action = Action(
            type = ActionType.Navigate,
            url = navigationUrl
        )
    )

    private fun sanitizeWebsite(website: String): String {
        val i = website.indexOf("//")
        return if (i > 0)
            website.substring(i + 2)
        else
            website
    }
}
