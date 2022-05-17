package com.wutsi.application.shared.ui

import com.wutsi.application.shared.Theme
import com.wutsi.application.shared.model.BottomNavigationBarModel
import com.wutsi.application.shared.service.TranslationUtil.getText
import com.wutsi.flutter.sdui.Action
import com.wutsi.flutter.sdui.BottomNavigationBar
import com.wutsi.flutter.sdui.BottomNavigationBarItem
import com.wutsi.flutter.sdui.WidgetAware
import com.wutsi.flutter.sdui.enums.ActionType

class BottomNavigationBarWidget(
    private val model: BottomNavigationBarModel
) : CompositeWidgetAware() {
    override fun toWidgetAware(): WidgetAware = toBottomNavigationBar()

    fun toBottomNavigationBar() = BottomNavigationBar(
        background = Theme.COLOR_PRIMARY,
        selectedItemColor = Theme.COLOR_WHITE,
        unselectedItemColor = Theme.COLOR_WHITE,
        items = listOfNotNull(
            BottomNavigationBarItem(
                icon = Theme.ICON_HOME,
                caption = getText("share-ui.bottom-nav-bar.home"),
                action = Action(
                    type = ActionType.Route,
                    url = "route:/~"
                )
            ),
            model.profileUrl?.let {
                BottomNavigationBarItem(
                    icon = Theme.ICON_PERSON,
                    caption = getText("share-ui.bottom-nav-bar.me"),
                    action = Action(
                        type = ActionType.Route,
                        url = it,
                    )
                )
            },
            model.transactionUrl?.let {
                BottomNavigationBarItem(
                    icon = Theme.ICON_HISTORY,
                    caption = getText("share-ui.bottom-nav-bar.transactions"),
                    action = Action(
                        type = ActionType.Route,
                        url = it
                    )
                )
            },
            model.settingsUrl?.let {
                BottomNavigationBarItem(
                    icon = Theme.ICON_SETTINGS,
                    caption = getText("share-ui.bottom-nav-bar.settings"),
                    action = Action(
                        type = ActionType.Route,
                        url = it
                    )
                )
            }
        )
    )
}
