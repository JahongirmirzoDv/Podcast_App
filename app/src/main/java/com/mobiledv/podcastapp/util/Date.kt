package com.mobiledv.podcastapp.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun Long.formatMillisecondsAsDate(
    pattern: String = "yyyy-MM-dd HH:mm:ss"
): String {
    val dateFormatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault())
    val instant = Instant.ofEpochMilli(this)
    return dateFormatter.format(instant)
}