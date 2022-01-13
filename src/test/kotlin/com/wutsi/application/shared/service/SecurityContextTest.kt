package com.wutsi.application.shared.service

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.account.WutsiAccountApi
import com.wutsi.platform.account.dto.Account
import com.wutsi.platform.account.dto.GetAccountResponse
import com.wutsi.platform.core.security.SubjectType
import com.wutsi.platform.core.security.WutsiPrincipal
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import kotlin.test.assertEquals

internal class SecurityContextTest {
    private val account: Account = Account(
        id = 1
    )
    private val principal = WutsiPrincipal(
        id = "1",
        _name = "Ray Sponsible",
        admin = false,
        tenantId = 1,
        type = SubjectType.USER
    )
    private lateinit var accountApi: WutsiAccountApi
    private lateinit var tenantProvider: TenantProvider
    private lateinit var context: SecurityContext

    @BeforeEach
    fun setUp() {
        tenantProvider = mock()

        accountApi = mock()
        doReturn(GetAccountResponse(account)).whenever(accountApi).getAccount(any())

        val authentication = mock<Authentication>()
        doReturn(principal).whenever(authentication).principal

        val ctx = mock<org.springframework.security.core.context.SecurityContext>()
        doReturn(authentication).whenever(ctx).authentication
        SecurityContextHolder.setContext(ctx)

        context = SecurityContext(accountApi, tenantProvider)
    }

    @Test
    fun currentAccountId() {
        assertEquals(1L, context.currentAccountId())
    }

    @Test
    fun currentAccount() {
        assertEquals(account, context.currentAccount())
    }

    @Test
    fun principal() {
        assertEquals(principal, context.principal())
    }

    @Test
    fun anonymous() {
        val ctx = mock<org.springframework.security.core.context.SecurityContext>()
        doReturn(null).whenever(ctx).authentication
        SecurityContextHolder.setContext(ctx)

        assertEquals("-1", context.principal().id)
        assertEquals(SubjectType.UNKNOWN, context.principal().type)
    }
}
