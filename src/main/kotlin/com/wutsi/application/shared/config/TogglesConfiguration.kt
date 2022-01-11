package com.wutsi.application.shared.config

import com.wutsi.application.shared.service.SecurityContext
import com.wutsi.application.shared.service.Toggles
import com.wutsi.application.shared.service.TogglesProvider
import com.wutsi.application.shared.spring.YamlPropertySourceFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource("classpath:toggles.yml", factory = YamlPropertySourceFactory::class)
class TogglesConfiguration(
    private val securityContext: SecurityContext
) {
    @Bean
    @ConfigurationProperties(prefix = "wutsi.toggles")
    fun toggles(): Toggles =
        Toggles()

    @Bean
    fun toggleProvider(): TogglesProvider =
        TogglesProvider(toggles(), securityContext)
}
