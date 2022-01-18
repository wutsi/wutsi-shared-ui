package com.wutsi.application.shared.ui

import com.wutsi.application.shared.Theme
import com.wutsi.application.shared.service.CategoryService
import com.wutsi.application.shared.service.PhoneUtil.format
import com.wutsi.application.shared.service.TogglesProvider
import com.wutsi.flutter.sdui.Action
import com.wutsi.flutter.sdui.Button
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
import com.wutsi.flutter.sdui.enums.ButtonType
import com.wutsi.flutter.sdui.enums.MainAxisAlignment
import com.wutsi.flutter.sdui.enums.MainAxisSize
import com.wutsi.flutter.sdui.enums.TextAlignment
import com.wutsi.platform.account.dto.Account
import org.springframework.context.i18n.LocaleContextHolder
import java.util.Locale

class ProfileCard(
    private val account: Account,
    private val phoneNumber: String? = null,
    private val showWebsite: Boolean = true,
    private val categoryService: CategoryService,
    private val togglesProvider: TogglesProvider
) : CompositeWidgetAware() {
    override fun toWidgetAware(): WidgetAware {
        val children = mutableListOf<WidgetAware>(
            Container(
                padding = 10.0,
                alignment = Alignment.Center,
                child = Avatar(
                    radius = 48.0,
                    textSize = 30.0,
                    text = account.displayName,
                    pictureUrl = account.pictureUrl,
                )
            ),
            Container(
                alignment = Alignment.Center,
                padding = 10.0,
                child = Row(
                    mainAxisAlignment = MainAxisAlignment.center,
                    children = listOf(
                        Text(
                            caption = account.displayName ?: "",
                            alignment = TextAlignment.Center,
                            size = Theme.TEXT_SIZE_X_LARGE,
                            color = Theme.COLOR_PRIMARY,
                            bold = true,
                        ),
                        if (account.retail && togglesProvider.isBusinessAccountEnabled())
                            Icon(
                                Theme.ICON_VERIFIED,
                                color = Theme.COLOR_PRIMARY,
                                size = Theme.TEXT_SIZE_X_LARGE
                            )
                        else
                            Container(),
                    ),
                )
            ),
        )

        // Phone
        if (phoneNumber != null) {
            children.add(
                Text(
                    caption = format(phoneNumber)!!,
                    alignment = TextAlignment.Center,
                    color = Theme.COLOR_GRAY,
                )
            )
        }

        // Bio
        if (!account.biography.isNullOrEmpty())
            children.add(
                Container(
                    padding = 10.0,
                    alignment = Alignment.Center,
                    child = Text(
                        alignment = TextAlignment.Center,
                        caption = account.biography!!
                    )
                )
            )

        // Web site
        if (showWebsite && !account.website.isNullOrEmpty())
            children.add(
                Button(
                    type = ButtonType.Text,
                    caption = sanitizeWebsite(account.website!!),
                    action = Action(
                        type = ActionType.Navigate,
                        url = account.website!!
                    )
                )
            )

        // More
        val more = mutableListOf<WidgetAware>()
        if (!account.country.isNullOrEmpty()) {
            val locale = LocaleContextHolder.getLocale()
            more.add(
                Container(
                    child = Row(
                        children = listOf(
                            Icon(code = Theme.ICON_LOCATION, size = 16.0),
                            Container(padding = 2.0),
                            Text(
                                caption = Locale(locale.language, account.country).getDisplayCountry(locale),
                                color = Theme.COLOR_GRAY,
                            )
                        ),
                        mainAxisSize = MainAxisSize.min
                    )
                )
            )
        }
        if (account.business && togglesProvider.isBusinessAccountEnabled()) {
            val category = categoryService.getTitle(account)
            if (category != null)
                more.add(
                    Container(
                        child = Row(
                            children = listOf(
                                Icon(code = Theme.ICON_BUSINESS, size = 16.0),
                                Container(padding = 2.0),
                                Text(
                                    caption = category,
                                    color = Theme.COLOR_GRAY,
                                )
                            ),
                            mainAxisSize = MainAxisSize.min
                        )
                    )
                )
        }
        if (more.isNotEmpty())
            children.add(
                Container(
                    padding = 10.0,
                    child = Wrap(
                        spacing = 20.0,
                        runSpacing = 10.0,
                        children = more,
                        direction = Axis.Horizontal
                    )
                )
            )

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
