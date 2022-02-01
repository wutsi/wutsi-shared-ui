package com.wutsi.application.shared.service

import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.net.URLEncoder

object PhoneUtil {
    fun toWhatsAppUrl(phoneNumber: String?, text: String? = null): String? {
        if (phoneNumber.isNullOrEmpty())
            return null

        val normalized = if (phoneNumber.startsWith("+"))
            phoneNumber.substring(1)
        else
            phoneNumber

        val query = if (text.isNullOrEmpty())
            ""
        else
            "?text=" + URLEncoder.encode(text, Charsets.UTF_8)
        return "https://wa.me/$normalized$query"
    }

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
