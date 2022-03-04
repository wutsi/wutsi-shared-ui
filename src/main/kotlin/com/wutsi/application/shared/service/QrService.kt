package com.wutsi.application.shared.service

import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles

open class QrService(
    private val env: Environment
) {
    open fun imageUrl(token: String): String? {
        val env = if (env.acceptsProfiles(Profiles.of("prod")))
            com.wutsi.platform.qr.Environment.PRODUCTION
        else
            com.wutsi.platform.qr.Environment.SANDBOX
        return "${env.url}/image/$token.png"
    }
}
