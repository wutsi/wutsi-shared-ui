package com.wutsi.application.shared.config

import com.wutsi.application.shared.service.SecurityContext
import com.wutsi.application.shared.service.TenantProvider
import com.wutsi.application.shared.service.Toggles
import com.wutsi.application.shared.service.TogglesProvider
import com.wutsi.application.shared.spring.YamlPropertySourceFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources
import org.springframework.core.env.Environment

@Configuration
@PropertySources(
    value = [
        PropertySource("classpath:toggles.yml", factory = YamlPropertySourceFactory::class),
        PropertySource(
            "classpath:toggles-\${spring.profiles.active}.yml",
            factory = YamlPropertySourceFactory::class,
            ignoreResourceNotFound = true
        )
    ]
)
class TogglesConfiguration(
    private val securityContext: SecurityContext,
    private val tenantProvider: TenantProvider,
    private val env: Environment
) {
    @Bean
    @ConfigurationProperties(prefix = "wutsi.toggles")
    fun toggles(): Toggles =
        Toggles()

    @Bean
    fun toggleProvider(): TogglesProvider =
        TogglesProvider(toggles(), tenantProvider, securityContext, env)
}
