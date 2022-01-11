package com.wutsi.application.shared.service

class URLBuilder(
    private val serverUrl: String
) {
    fun build(path: String) = build(serverUrl, path)

    fun build(prefix: String, path: String): String {
        val xprefix = if (prefix.endsWith("/"))
            prefix.substring(0, prefix.length - 1)
        else
            prefix

        val xpath = if (path.startsWith("/"))
            path.substring(1)
        else
            path

        return "$xprefix/$xpath"
    }
}
