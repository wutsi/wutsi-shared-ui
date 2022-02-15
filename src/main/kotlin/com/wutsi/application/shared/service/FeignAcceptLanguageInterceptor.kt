package com.wutsi.application.shared.service

import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.http.HttpHeaders
import javax.servlet.http.HttpServletRequest

class FeignAcceptLanguageInterceptor(
    private val request: HttpServletRequest
) : RequestInterceptor {
    override fun apply(template: RequestTemplate) {
        val language = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE)
        if (language != null)
            template.header(HttpHeaders.ACCEPT_LANGUAGE, language)
    }
}
