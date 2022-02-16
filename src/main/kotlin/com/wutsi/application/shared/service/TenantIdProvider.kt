package com.wutsi.application.shared.service

import com.wutsi.platform.core.tracing.TracingContext
import com.wutsi.platform.tenant.WutsiTenantApi
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles
import javax.servlet.http.HttpServletRequest

open class TenantIdProvider(
    private val tracingContext: TracingContext,
    private val tenantApi: WutsiTenantApi,
    private val env: Environment,
    private val request: HttpServletRequest
) {
    open fun get(): Long {
        // Resolve from tracing context
        val tenantId = tracingContext.tenantId()
        if (tenantId != null)
            return tenantId.toLong()

        // Resolve from host
        return if (isLocal())
            1L
        else
            tenantApi.listTenants().tenants
                .find { it.webappUrl.contains(request.serverName) }
                ?.id
                ?: throw IllegalStateException("Not tenant found - ${request.serverName}")
    }

    private fun isLocal(): Boolean =
        !isProd() && !isTest()

    private fun isProd(): Boolean =
        env.acceptsProfiles(Profiles.of("prod"))

    private fun isTest(): Boolean =
        env.acceptsProfiles(Profiles.of("test"))
}
