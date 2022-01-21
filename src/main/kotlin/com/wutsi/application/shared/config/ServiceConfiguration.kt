package com.wutsi.application.shared.config

import com.wutsi.application.shared.service.CategoryService
import com.wutsi.application.shared.service.CityService
import com.wutsi.application.shared.service.RequestLocaleResolver
import com.wutsi.application.shared.service.SecurityContext
import com.wutsi.application.shared.service.SharedUIMapper
import com.wutsi.application.shared.service.TenantProvider
import com.wutsi.application.shared.service.URLBuilder
import com.wutsi.platform.account.WutsiAccountApi
import com.wutsi.platform.core.tracing.TracingContext
import com.wutsi.platform.tenant.WutsiTenantApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.web.servlet.LocaleResolver

@Configuration
class ServiceConfiguration(
    private val tenantApi: WutsiTenantApi,
    private val tracingContext: TracingContext,
    private val accountApi: WutsiAccountApi,

    @Value("\${wutsi.application.server-url}") private val serverUrl: String,
) {
    @Bean
    fun categoryService(): CategoryService =
        CategoryService()

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
        SecurityContext(accountApi, tenantProvider())

    @Bean
    fun sharedUIMapper(): SharedUIMapper =
        SharedUIMapper(categoryService())

    @Bean
    fun tenantProvider(): TenantProvider =
        TenantProvider(tenantApi, tracingContext)

    @Bean
    fun urlBuilder(): URLBuilder =
        URLBuilder(serverUrl)
}
