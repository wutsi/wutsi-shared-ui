package com.wutsi.application.shared.service

object PhoneUtil {
    fun sanitize(phoneNumber: String): String {
        val tmp = phoneNumber.trim()
        return if (tmp.startsWith("+"))
            tmp
        else
            "+$tmp"
    }
}
