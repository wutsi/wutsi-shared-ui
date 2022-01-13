package com.wutsi.application.shared.service

import com.wutsi.platform.account.WutsiAccountApi
import com.wutsi.platform.account.dto.Account
import com.wutsi.platform.core.security.SubjectType
import com.wutsi.platform.core.security.WutsiPrincipal
import org.springframework.security.core.context.SecurityContextHolder

open class SecurityContext(
    private val accountApi: WutsiAccountApi,
    private val tenantProvider: TenantProvider
) {
    open fun currentAccountId(): Long =
        principal().id.toLong()

    open fun currentAccount(): Account =
        accountApi.getAccount(currentAccountId()).account

    open fun principal(): WutsiPrincipal {
        val principal = SecurityContextHolder.getContext()?.authentication?.principal
        return if (principal is WutsiPrincipal)
            principal
        else
            WutsiPrincipal(
                id = "-1",
                _name = "",
                type = SubjectType.UNKNOWN,
                tenantId = tenantProvider.tenantId(),
                admin = false
            )
    }
}
