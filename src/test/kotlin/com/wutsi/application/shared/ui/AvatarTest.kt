package com.wutsi.application.shared.ui

import com.wutsi.application.shared.model.AccountModel
import com.wutsi.flutter.sdui.CircleAvatar
import com.wutsi.flutter.sdui.Image
import com.wutsi.flutter.sdui.Text
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

internal class AvatarTest {
    @Test
    fun picture() {
        val widget = Avatar(
            model = AccountModel(pictureUrl = "https://img.com/1.png"),
            radius = 40.0
        ).toWidgetAware() as CircleAvatar

        assertEquals(40.0, widget.radius)
        assertTrue(widget.child is Image)
        assertEquals("https://img.com/1.png", (widget.child as Image).url)
    }

    @Test
    fun noPicture() {
        val widget = Avatar(
            model = AccountModel(displayName = "Ray Sponsible"),
            radius = 12.0
        ).toWidgetAware() as CircleAvatar

        assertEquals(12.0, widget.radius)
        assertTrue(widget.child is Text)
        assertEquals("RS", (widget.child as Text).caption)
        assertEquals(true, (widget.child as Text).bold)
        assertEquals(14.0, (widget.child as Text).size)
    }
}
