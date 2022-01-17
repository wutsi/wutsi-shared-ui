package com.wutsi.application.shared.service

import com.google.i18n.phonenumbers.PhoneNumberUtil

object PhoneUtil {
    fun sanitize(phoneNumber: String): String {
        val tmp = phoneNumber.trim()
        return if (tmp.startsWith("+"))
            tmp
        else
            "+$tmp"
    }

    fun format(phoneNumber: String?, country: String? = null): String? {
        if (phoneNumber == null)
            return null

        val phoneNumberUtil = PhoneNumberUtil.getInstance()
        val number = phoneNumberUtil.parse(phoneNumber, country ?: "")
        return phoneNumberUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
    }
}
