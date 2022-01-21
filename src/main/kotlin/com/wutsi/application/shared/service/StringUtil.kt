package com.wutsi.application.shared.service

object StringUtil {
    fun initials(fullName: String?): String {
        if (fullName.isNullOrEmpty())
            return ""

        val xfullName = fullName.trim()
        val index = xfullName.lastIndexOf(' ')
        return if (index > 0)
            (xfullName.substring(0, 1) + xfullName.substring(index + 1, index + 2)).uppercase()
        else
            xfullName.substring(0, 1).uppercase()
    }

    fun capitalizeFirstLetter(str: String?): String =
        if (str.isNullOrEmpty())
            ""
        else
            str.uppercase().substring(0, 1) + str.substring(1)

    fun firstName(displayName: String?): String {
        if (displayName == null)
            return ""

        val i = displayName.indexOf(' ')
        return if (i > 0) displayName.substring(0, i) else displayName
    }
}
