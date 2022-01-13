package com.wutsi.application.shared.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PhoneUtilTest {
    @Test
    fun sanitize() {
        assertEquals("+15147580000", PhoneUtil.sanitize(" 15147580000 "))
        assertEquals("+15147580000", PhoneUtil.sanitize("+15147580000"))
        assertEquals("+15147580000", PhoneUtil.sanitize("15147580000"))
    }
}
