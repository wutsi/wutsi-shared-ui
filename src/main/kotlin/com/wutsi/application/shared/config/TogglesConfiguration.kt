package com.wutsi.application.shared.config

import com.wutsi.application.shared.service.TenantProvider
import com.wutsi.application.shared.service.TogglesProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TogglesConfiguration(
    private val tenantProvider: TenantProvider,
) {
    @Bean
    fun toggleProvider(): TogglesProvider =
        TogglesProvider(tenantProvider)
}
