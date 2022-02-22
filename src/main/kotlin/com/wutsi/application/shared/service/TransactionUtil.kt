package com.wutsi.application.shared.service

import org.springframework.context.i18n.LocaleContextHolder
import java.text.MessageFormat
import java.util.Locale
import java.util.ResourceBundle

object TransactionUtil {
    private val defaultBundle = ResourceBundle.getBundle("shared-ui-messages")
    private val frBundle = ResourceBundle.getBundle("shared-ui-messages", Locale("fr"))

    fun getText(key: String, args: Array<String> = emptyArray()): String =
        try {
            val locale = LocaleContextHolder.getLocale()
            val bundle = if (locale.language == "fr")
                frBundle
            else
                defaultBundle

            MessageFormat.format(bundle.getString(key), *args)
        } catch (ex: Exception) {
            key
        }
}
