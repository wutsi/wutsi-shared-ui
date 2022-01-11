package com.wutsi.application.shared.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class StringUtilTest {
    @Test
    fun test() {
        assertEquals("RS", StringUtil.initials("Ray Sponsible"))
        assertEquals("RS", StringUtil.initials("ray sponsible"))
        assertEquals("R", StringUtil.initials("Ray"))
    }
}
