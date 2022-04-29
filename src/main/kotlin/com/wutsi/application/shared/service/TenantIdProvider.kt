package com.wutsi.application.shared.service

import com.wutsi.platform.core.tracing.TracingContext
import com.wutsi.platform.tenant.WutsiTenantApi
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles
import javax.servlet.http.HttpServletRequest

@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
open class TenantIdProvider(
    private val tracingContext: TracingContext,
    private val tenantApi: WutsiTenantApi,
    private val env: Environment,
    private val request: HttpServletRequest
) {
    private var id: Long? = null

    open fun get(): Long =
        id ?: resolveId()

    private fun resolveId(): Long {
        // Resolve from tracing context
        val tenantId = tracingContext.tenantId()
        if (tenantId != null)
            return tenantId.toLong()

        // Resolve from host
        id = if (isLocal())
            1L
        else
            tenantApi.listTenants().tenants
                .find { it.webappUrl.contains(request.serverName) }
                ?.id
                ?: throw IllegalStateException("Not tenant found - ${request.serverName}")

        return id!!
    }

    private fun isLocal(): Boolean =
        !isProd() && !isTest()

    private fun isProd(): Boolean =
        env.acceptsProfiles(Profiles.of("prod"))

    private fun isTest(): Boolean =
        env.acceptsProfiles(Profiles.of("test"))
}
