package com.wutsi.application.shared.service

import com.wutsi.platform.tenant.WutsiTenantApi
import com.wutsi.platform.tenant.dto.MobileCarrier
import com.wutsi.platform.tenant.dto.Tenant

open class TenantProvider(
    private val tenantApi: WutsiTenantApi,
    private val tenantIdProvider: TenantIdProvider,
) {
    open fun tenantId(): Long =
        tenantIdProvider.get()

    open fun get(): Tenant =
        tenantApi.getTenant(tenantId()).tenant

    open fun mobileCarriers(tenant: Tenant) =
        tenant.mobileCarriers.filter { it.countries.any { it in tenant.countries } }

    open fun logo(carrier: MobileCarrier): String? =
        carrier.logos.find { it.type == "PICTORIAL" }?.url

    open fun logo(tenant: Tenant): String? =
        tenant.logos.find { it.type == "PICTORIAL" }?.url
}
