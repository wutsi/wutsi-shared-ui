package com.wutsi.application.shared.config

import com.wutsi.application.shared.service.FeignAcceptLanguageInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.servlet.http.HttpServletRequest

@Configuration
class AcceptLanguageConfiguration(
    private val request: HttpServletRequest
) {
    @Bean
    fun feignAcceptLanguageInterceptor(): FeignAcceptLanguageInterceptor =
        FeignAcceptLanguageInterceptor(request)
}
