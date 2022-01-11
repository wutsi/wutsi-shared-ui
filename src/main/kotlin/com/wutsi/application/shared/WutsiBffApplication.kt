package com.wutsi.application.shared

import com.wutsi.application.shared.config.PhoneNumberConfiguration
import com.wutsi.application.shared.config.ServiceConfiguration
import com.wutsi.application.shared.config.TogglesConfiguration
import org.springframework.context.annotation.Import

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@Import(
    value = [
        ServiceConfiguration::class,
        TogglesConfiguration::class,
        PhoneNumberConfiguration::class
    ]
)
annotation class WutsiBffApplication
