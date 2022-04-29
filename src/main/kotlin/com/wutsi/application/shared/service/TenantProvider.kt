package com.wutsi.application.shared.service

import com.wutsi.platform.tenant.WutsiTenantApi
import com.wutsi.platform.tenant.dto.MobileCarrier
import com.wutsi.platform.tenant.dto.Tenant
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode

@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
open class TenantProvider(
    private val tenantApi: WutsiTenantApi,
    private val tenantIdProvider: TenantIdProvider,
) {
    private var tenant: Tenant? = null

    open fun tenantId(): Long =
        tenantIdProvider.get()

    open fun get(): Tenant =
        if (tenant == null || tenant?.id != tenantIdProvider.get())
            resolveTenant()
        else
            tenant!!

    private fun resolveTenant(): Tenant {
        tenant = tenantApi.getTenant(tenantId()).tenant
        return tenant!!
    }

    open fun mobileCarriers(tenant: Tenant) =
        tenant.mobileCarriers.filter { it.countries.any { it in tenant.countries } }

    open fun logo(carrier: MobileCarrier): String? =
        carrier.logos.find { it.type == "PICTORIAL" }?.url

    open fun logo(tenant: Tenant): String? =
        tenant.logos.find { it.type == "PICTORIAL" }?.url
}
