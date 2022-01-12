package com.wutsi.application.shared.service

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.Locale
import javax.servlet.http.HttpServletRequest

internal class RequestLocaleResolverTest {
    private val resolver = RequestLocaleResolver()

    @Test
    fun resolveLocale() {
        val request = mock<HttpServletRequest>()
        val locale = Locale("a", "b")
        doReturn(locale).whenever(request).locale
        assertEquals(locale, resolver.resolveLocale(request))
    }

    @Test
    fun resolveLocaleNull() {
        val request = mock<HttpServletRequest>()
        doReturn(null).whenever(request).locale
        assertEquals(Locale.US, resolver.resolveLocale(request))
    }
}
