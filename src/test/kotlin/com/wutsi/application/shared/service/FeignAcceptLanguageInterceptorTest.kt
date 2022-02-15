package com.wutsi.application.shared.service

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import feign.RequestTemplate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE
import javax.servlet.http.HttpServletRequest

internal class FeignAcceptLanguageInterceptorTest {
    private lateinit var request: HttpServletRequest
    private lateinit var interceptor: FeignAcceptLanguageInterceptor
    private lateinit var template: RequestTemplate

    @BeforeEach
    fun setUp() {
        request = mock()
        interceptor = FeignAcceptLanguageInterceptor(request)
        template = RequestTemplate()
    }

    @Test
    fun noHeader() {
        interceptor.apply(template)

        assertNull(template.headers()[ACCEPT_LANGUAGE])
    }

    @Test
    fun header() {
        doReturn("fr").whenever(request).getHeader(ACCEPT_LANGUAGE)

        interceptor.apply(template)

        assertEquals(true, template.headers()[ACCEPT_LANGUAGE]?.contains("fr"))
    }
}
