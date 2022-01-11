package com.wutsi.application.shared.service

object StringUtil {
    fun initials(fullName: String?): String {
        if (fullName == null)
            return ""

        val xfullName = fullName.trim()
        val index = xfullName.lastIndexOf(' ')
        return if (index > 0)
            (xfullName.substring(0, 1) + xfullName.substring(index + 1, index + 2)).uppercase()
        else
            xfullName.substring(0, 1).uppercase()
    }
}
