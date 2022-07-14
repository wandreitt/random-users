package com.andrei.wegroszta.randomusers.ext

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

private const val MILLIS_IN_SECOND = 1000
private const val MILLIS_IN_MINUTE = MILLIS_IN_SECOND * 60
private const val MILLIS_IN_HOUR = MILLIS_IN_MINUTE * 60

fun Long.toLocalStringHoursMinutes(): String {
    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return dateFormat.format(Date(this))
}

fun Long.localDateTimestampToUTCTimestamp(): Long {
    return this - Calendar.getInstance().timeZone.getOffset(this)
}

fun Int.minutesToMillis(): Int = this * MILLIS_IN_MINUTE

fun Int.hoursToMillis(): Int = this * MILLIS_IN_HOUR

fun Long.convertTimestampToOffsetHourMinutes(
    timezoneOffset: String
): String {
    val parts = timezoneOffset.split(":")
    val hours = parts[0].toInt()
    val minutes = parts[1].toInt()

    val shouldAdd = hours > 0

    val absoluteHours = hours.absoluteValue
    val absOffset = absoluteHours.hoursToMillis() + minutes.minutesToMillis()
    val offset = if (shouldAdd) absOffset else -absOffset

    val nowInUTC = this.localDateTimestampToUTCTimestamp()
    val nowWithOffset = nowInUTC + offset

    return nowWithOffset.toLocalStringHoursMinutes()
}
