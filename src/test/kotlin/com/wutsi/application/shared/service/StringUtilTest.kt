package com.wutsi.application.shared.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class StringUtilTest {
    @Test
    fun initials() {
        assertEquals("RS", StringUtil.initials("Ray Sponsible"))
        assertEquals("RS", StringUtil.initials("ray sponsible"))
        assertEquals("R", StringUtil.initials("Ray"))
        assertEquals("", StringUtil.initials("     "))
        assertEquals("", StringUtil.initials(""))
        assertEquals("", StringUtil.initials(null))
    }

    @Test
    fun capitalizeFirstLetter() {
        assertEquals("Ray sponsible", StringUtil.capitalizeFirstLetter("ray sponsible"))
        assertEquals("Ray", StringUtil.capitalizeFirstLetter("ray"))
        assertEquals("", StringUtil.capitalizeFirstLetter(""))
        assertEquals("", StringUtil.capitalizeFirstLetter(null))
    }

    @Test
    fun firstName() {
        assertEquals("Ray", StringUtil.firstName("Ray sponsible"))
        assertEquals("Ray", StringUtil.firstName("Ray"))
        assertEquals("", StringUtil.firstName(""))
        assertEquals("", StringUtil.firstName(null))
    }
}
