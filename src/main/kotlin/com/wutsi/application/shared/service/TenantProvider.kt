package com.wutsi.application.shared.service

import com.wutsi.platform.core.tracing.TracingContext
import com.wutsi.platform.tenant.WutsiTenantApi
import com.wutsi.platform.tenant.dto.MobileCarrier
import com.wutsi.platform.tenant.dto.Tenant

open class TenantProvider(
    private val tenantApi: WutsiTenantApi,
    private val tracingContext: TracingContext,
) {
    open fun tenantId(): Long =
        tracingContext.tenantId()!!.toLong()

    open fun get(): Tenant =
        tenantApi.getTenant(tenantId()).tenant

    open fun mobileCarriers(tenant: Tenant) =
        tenant.mobileCarriers.filter { it.countries.any { it in tenant.countries } }

    open fun logo(carrier: MobileCarrier): String? =
        carrier.logos.find { it.type == "PICTORIAL" }?.url
}
