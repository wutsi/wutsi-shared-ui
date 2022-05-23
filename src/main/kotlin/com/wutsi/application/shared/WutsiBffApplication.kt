package com.wutsi.application.shared

import com.wutsi.application.shared.config.AcceptLanguageConfiguration
import com.wutsi.application.shared.config.AccountApiConfiguration
import com.wutsi.application.shared.config.PhoneNumberConfiguration
import com.wutsi.application.shared.config.SecurityApiConfiguration
import com.wutsi.application.shared.config.ServiceConfiguration
import com.wutsi.application.shared.config.TenantApiConfiguration
import com.wutsi.application.shared.config.TenantConfiguration
import com.wutsi.application.shared.config.TogglesConfiguration
import org.springframework.context.annotation.Import

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@Import(
    value = [
        AccountApiConfiguration::class,
        AcceptLanguageConfiguration::class,
        PhoneNumberConfiguration::class,
        SecurityApiConfiguration::class,
        ServiceConfiguration::class,
        TenantApiConfiguration::class,
        TenantConfiguration::class,
        TogglesConfiguration::class,
    ]
)
annotation class WutsiBffApplication
