package com.wutsi.application.shared.service

import org.springframework.web.servlet.LocaleResolver
import java.util.Locale
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

open class RequestLocaleResolver : LocaleResolver {
    override fun resolveLocale(request: HttpServletRequest): Locale =
        request.locale ?: Locale.US

    override fun setLocale(request: HttpServletRequest, response: HttpServletResponse?, locale: Locale?) {
        // NOTING
    }
}
