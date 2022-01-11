package com.wutsi.application.shared.config

import com.google.i18n.phonenumbers.PhoneNumberUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PhoneNumberConfiguration {
    @Bean
    fun phoneNumberUtil(): PhoneNumberUtil =
        PhoneNumberUtil.getInstance()
}
