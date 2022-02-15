package com.wutsi.application.shared

import com.wutsi.application.shared.config.AcceptLanguageConfiguration
import com.wutsi.application.shared.config.PhoneNumberConfiguration
import com.wutsi.application.shared.config.ServiceConfiguration
import com.wutsi.application.shared.config.TogglesConfiguration
import org.springframework.context.annotation.Import

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@Import(
    value = [
        AcceptLanguageConfiguration::class,
        PhoneNumberConfiguration::class,
        ServiceConfiguration::class,
        TogglesConfiguration::class,
    ]
)
annotation class WutsiBffApplication
