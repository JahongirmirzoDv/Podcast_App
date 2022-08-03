package com.mobiledv.podcastapp.util

fun Long.toDurationMinutes(): String {
    val minutes = (this / 60).toInt()

    return "$minutes min"
}