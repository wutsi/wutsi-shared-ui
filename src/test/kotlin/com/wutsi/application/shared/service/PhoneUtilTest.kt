package com.wutsi.application.shared.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

internal class PhoneUtilTest {
    @Test
    fun sanitize() {
        assertEquals("+15147580000", PhoneUtil.sanitize(" 15147580000 "))
        assertEquals("+15147580000", PhoneUtil.sanitize("+15147580000"))
        assertEquals("+15147580000", PhoneUtil.sanitize("15147580000"))
    }

    @Test
    fun format() {
        assertNull(PhoneUtil.format(null, "CM"))
        assertEquals("+1 514-758-0000", PhoneUtil.format("+15147580000", "CA"))
        assertEquals("+237 6 70 00 00 11", PhoneUtil.format("+237670000011", "CM"))
    }
}
