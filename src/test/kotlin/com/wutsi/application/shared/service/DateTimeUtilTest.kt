package com.wutsi.application.shared.service

import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.time.ZoneOffset
import kotlin.test.assertEquals

internal class DateTimeUtilTest {
    @Test
    fun convertDouala() {
        val date = OffsetDateTime.of(2020, 10, 1, 19, 0, 0, 0, ZoneOffset.UTC)

        val xdate = DateTimeUtil.convert(date, "Africa/Douala")
        assertEquals(date.year, xdate.year)
        assertEquals(date.month, xdate.month)
        assertEquals(date.dayOfMonth, xdate.dayOfMonth)
        assertEquals(date.hour + 1, xdate.hour)
        assertEquals(date.minute, xdate.minute)
    }

    @Test
    fun convertMontreal() {
        val date = OffsetDateTime.of(2020, 10, 1, 19, 0, 0, 0, ZoneOffset.UTC)

        val xdate = DateTimeUtil.convert(date, "America/Montreal")
        assertEquals(date.year, xdate.year)
        assertEquals(date.month, xdate.month)
        assertEquals(date.dayOfMonth, xdate.dayOfMonth)
        assertEquals(date.hour - 4, xdate.hour)
        assertEquals(date.minute, xdate.minute)
    }
}
