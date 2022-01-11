package com.wutsi.application.shared.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class URLBuilderTest {
    @Test
    fun test() {
        val urlBuilder = URLBuilder("https://wwww.google.com")

        assertEquals("https://wwww.google.com/a/b.png", urlBuilder.build("a/b.png"))
        assertEquals("https://wwww.google.com/a/b.png", urlBuilder.build("/a/b.png"))
    }

    @Test
    fun testWithCustomPrefix() {
        val urlBuilder = URLBuilder("https://wwww.yahoo.com")

        assertEquals("https://wwww.google.com/a/b.png", urlBuilder.build("https://wwww.google.com", "a/b.png"))
        assertEquals("https://wwww.google.com/a/b.png", urlBuilder.build("https://wwww.google.com", "/a/b.png"))
        assertEquals("https://wwww.google.com/a/b.png", urlBuilder.build("https://wwww.google.com/", "a/b.png"))
        assertEquals("https://wwww.google.com/a/b.png", urlBuilder.build("https://wwww.google.com/", "/a/b.png"))
    }
}
