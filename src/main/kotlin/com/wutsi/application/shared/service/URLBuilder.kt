package com.wutsi.application.shared.service

open class URLBuilder(
    private val serverUrl: String
) {
    open fun build(path: String) = build(serverUrl, path)

    open fun build(prefix: String, path: String): String {
        val xprefix = if (prefix.endsWith("/"))
            prefix.substring(0, prefix.length - 1)
        else
            prefix

        val xpath = if (path.startsWith("/"))
            path.substring(1)
        else
            path

        return if (xpath.isEmpty()) xprefix else "$xprefix/$xpath"
    }
}
