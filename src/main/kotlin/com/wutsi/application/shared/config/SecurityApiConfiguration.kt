package com.wutsi.application.shared.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.platform.core.tracing.feign.FeignTracingRequestInterceptor
import com.wutsi.platform.core.util.feign.Custom5XXErrorDecoder
import com.wutsi.platform.security.Environment.PRODUCTION
import com.wutsi.platform.security.Environment.SANDBOX
import com.wutsi.platform.security.WutsiSecurityApi
import com.wutsi.platform.security.WutsiSecurityApiBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles

@Configuration
class SecurityApiConfiguration(
    private val tracingRequestInterceptor: FeignTracingRequestInterceptor,
    private val mapper: ObjectMapper,
    private val env: Environment
) {
    @Bean
    fun securityApi(): WutsiSecurityApi =
        WutsiSecurityApiBuilder().build(
            env = environment(),
            mapper = mapper,
            interceptors = listOf(
                tracingRequestInterceptor,
            ),
            errorDecoder = Custom5XXErrorDecoder()
        )

    private fun environment(): com.wutsi.platform.security.Environment =
        if (env.acceptsProfiles(Profiles.of("prod")))
            PRODUCTION
        else
            SANDBOX
}
