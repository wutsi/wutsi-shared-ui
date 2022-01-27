package com.wutsi.application.shared.service

import java.time.OffsetDateTime
import java.time.ZoneId

object DateTimeUtil {
    fun convert(dateTime: OffsetDateTime, timezoneId: String?): OffsetDateTime =
        if (timezoneId == null)
            dateTime
        else
            dateTime.atZoneSameInstant(ZoneId.of(timezoneId)).toOffsetDateTime()
}
