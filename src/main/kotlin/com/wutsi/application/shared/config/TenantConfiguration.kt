package com.wutsi.application.shared.config

import com.wutsi.application.shared.service.TenantIdProvider
import com.wutsi.application.shared.service.TenantProvider
import com.wutsi.platform.core.tracing.TracingContext
import com.wutsi.platform.tenant.WutsiTenantApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import javax.servlet.http.HttpServletRequest

@Configuration
class TenantConfiguration(
    private val tenantApi: WutsiTenantApi,
    private val tracingContext: TracingContext,
    private val request: HttpServletRequest,
    private val env: Environment,
) {
    @Bean
    fun tenantIdProvider(): TenantIdProvider =
        TenantIdProvider(tracingContext, tenantApi, env, request)

    @Bean
    fun tenantProvider(): TenantProvider =
        TenantProvider(tenantApi, tenantIdProvider())
}
