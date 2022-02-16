package com.wutsi.application.shared.service

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.core.tracing.TracingContext
import com.wutsi.platform.tenant.WutsiTenantApi
import com.wutsi.platform.tenant.dto.ListTenantResponse
import com.wutsi.platform.tenant.dto.TenantSummary
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles
import javax.servlet.http.HttpServletRequest
import kotlin.test.assertEquals

internal class TenantIdProviderTest {
    private lateinit var tracingContext: TracingContext
    private lateinit var tenantApi: WutsiTenantApi
    private lateinit var env: Environment
    private lateinit var request: HttpServletRequest
    private lateinit var tenantIdProvider: TenantIdProvider

    @BeforeEach
    fun setUp() {
        tracingContext = mock()
        tenantApi = mock()
        env = mock()
        request = mock()

        tenantIdProvider = TenantIdProvider(tracingContext, tenantApi, env, request)

        val tenants = listOf(
            TenantSummary(id = 11, webappUrl = "http://www.wutsi.com"),
            TenantSummary(id = 22, webappUrl = "http://test.wutsi.com")
        )
        doReturn(ListTenantResponse(tenants)).whenever(tenantApi).listTenants()
    }

    @Test
    fun `get tenant from tracing context`() {
        doReturn("1").whenever(tracingContext).tenantId()

        assertEquals(1L, tenantIdProvider.get())
    }

    @Test
    fun `local tenant is 1`() {
        doReturn(false).whenever(env).acceptsProfiles(Profiles.of("prod"))
        doReturn(false).whenever(env).acceptsProfiles(Profiles.of("test"))

        assertEquals(1L, tenantIdProvider.get())
    }

    @Test
    fun `PROD tenant from request host`() {
        doReturn("www.wutsi.com").whenever(request).serverName
        doReturn(true).whenever(env).acceptsProfiles(Profiles.of("prod"))
        doReturn(false).whenever(env).acceptsProfiles(Profiles.of("test"))

        assertEquals(11L, tenantIdProvider.get())
    }

    @Test
    fun `TEST tenant from request host`() {
        doReturn("test.wutsi.com").whenever(request).serverName
        doReturn(false).whenever(env).acceptsProfiles(Profiles.of("prod"))
        doReturn(true).whenever(env).acceptsProfiles(Profiles.of("test"))

        assertEquals(22L, tenantIdProvider.get())
    }
}
