package com.wutsi.application.shared.ui

import com.wutsi.flutter.sdui.CircleAvatar
import com.wutsi.flutter.sdui.Image
import com.wutsi.flutter.sdui.Text
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

internal class AvatarTest {
    @Test
    fun picture() {
        val widget = Avatar(pictureUrl = "https://img.com/1.png", radius = 12.0).toWidgetAware() as CircleAvatar

        assertEquals(12.0, widget.radius)
        assertTrue(widget.child is Image)
        assertEquals("https://img.com/1.png", (widget.child as Image).url)
    }

    @Test
    fun noPicture() {
        val widget = Avatar(text = "Ray Sponsible", radius = 12.0, textSize = 10.0).toWidgetAware() as CircleAvatar

        assertEquals(12.0, widget.radius)
        assertTrue(widget.child is Text)
        assertEquals("RS", (widget.child as Text).caption)
        assertEquals(true, (widget.child as Text).bold)
        assertEquals(10.0, (widget.child as Text).size)
    }
}
