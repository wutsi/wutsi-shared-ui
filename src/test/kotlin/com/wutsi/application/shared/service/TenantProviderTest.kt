package com.wutsi.application.shared.service

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.tenant.WutsiTenantApi
import com.wutsi.platform.tenant.dto.GetTenantResponse
import com.wutsi.platform.tenant.dto.Logo
import com.wutsi.platform.tenant.dto.MobileCarrier
import com.wutsi.platform.tenant.dto.PhonePrefix
import com.wutsi.platform.tenant.dto.Tenant
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TenantProviderTest {
    private lateinit var tenantApi: WutsiTenantApi
    private lateinit var provider: TenantProvider
    private lateinit var tenant: Tenant
    private lateinit var mobileCarrier1: MobileCarrier
    private lateinit var mobileCarrier2: MobileCarrier
    private lateinit var mobileCarrier3: MobileCarrier
    private lateinit var mobileCarrier4: MobileCarrier
    private lateinit var tenantIdProvider: TenantIdProvider

    @BeforeEach
    fun setUp() {
        tenantIdProvider = mock()
        doReturn(1L).whenever(tenantIdProvider).get()

        mobileCarrier1 = MobileCarrier(
            code = "OM",
            countries = listOf("CM", "GB"),
            logos = listOf(
                Logo(type = "PICTORIAL", url = "https://www.goole.com/om/images/1.png"),
                Logo(type = "WORDMAP", url = "https://www.goole.com/om/images/2.png"),
            ),
            phonePrefixes = listOf(
                PhonePrefix(
                    country = "CA",
                    prefixes = listOf("+1"),
                ),
                PhonePrefix(
                    country = "CM",
                    prefixes = listOf("+237")
                )
            )
        )
        mobileCarrier2 = MobileCarrier(code = "MTN", countries = listOf("CM", "GB", "NG"))
        mobileCarrier3 = MobileCarrier(code = "CAMTEL", countries = listOf("CM"))
        mobileCarrier4 = MobileCarrier(code = "NEXXTEL", countries = listOf("VT"))
        tenant = Tenant(
            id = 1,
            name = "test",
            logos = listOf(
                Logo(type = "PICTORIAL", url = "https://www.goole.com/images/1.png"),
                Logo(type = "WORDMAP", url = "https://www.goole.com/images/2.png"),
            ),
            countries = listOf("CM"),
            languages = listOf("en", "fr"),
            currency = "XAF",
            currencySymbol = "CFA",
            dateFormat = "dd MMM yyyy",
            timeFormat = "HH:mm",
            dateTimeFormat = "dd MMM yyyy, HH:mm",
            domainName = "www.wutsi.com",
            mobileCarriers = listOf(mobileCarrier1, mobileCarrier2, mobileCarrier3, mobileCarrier4)
        )
        tenantApi = mock()
        doReturn(GetTenantResponse(tenant)).whenever(tenantApi).getTenant(any())

        provider = TenantProvider(tenantApi, tenantIdProvider)
    }

    @Test
    fun tenantId() {
        assertEquals(1, provider.tenantId())
    }

    @Test
    fun get() {
        assertEquals(tenant, provider.get())
    }

    @Test
    fun mobileCarriers() {
        assertEquals(listOf(mobileCarrier1, mobileCarrier2, mobileCarrier3), provider.mobileCarriers(tenant))
    }

    @Test
    fun tenantLogo() {
        assertEquals("https://www.goole.com/images/1.png", provider.logo(tenant))
    }

    @Test
    fun carrierLogo() {
        assertEquals("https://www.goole.com/om/images/1.png", provider.logo(mobileCarrier1))
    }
}
