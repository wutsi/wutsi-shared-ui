package com.wutsi.application.shared.config

import com.wutsi.application.shared.service.CityService
import com.wutsi.application.shared.service.RequestLocaleResolver
import com.wutsi.application.shared.service.SecurityContext
import com.wutsi.application.shared.service.SharedUIMapper
import com.wutsi.application.shared.service.TenantProvider
import com.wutsi.application.shared.service.URLBuilder
import com.wutsi.platform.account.WutsiAccountApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.web.servlet.LocaleResolver

@Configuration
class ServiceConfiguration(
    private val tenantProvider: TenantProvider,
    private val accountApi: WutsiAccountApi,

    @Value("\${wutsi.application.server-url}") private val serverUrl: String,
) {
    @Bean
    fun cityService(): CityService =
        CityService()

    @Bean
    fun localeResolver(): LocaleResolver =
        RequestLocaleResolver()

    @Bean
    fun messageSource(): ResourceBundleMessageSource? {
        val messageSource = ResourceBundleMessageSource()
        messageSource.setBasename("messages")
        return messageSource
    }

    @Bean
    fun securityContext(): SecurityContext =
        SecurityContext(accountApi, tenantProvider)

    @Bean
    fun sharedUIMapper(): SharedUIMapper =
        SharedUIMapper(accountApi, cityService())

    @Bean
    fun urlBuilder(): URLBuilder =
        URLBuilder(serverUrl)
}
