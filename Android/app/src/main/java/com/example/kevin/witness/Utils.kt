package com.example.kevin.witness

import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun currentUtcTime8601(): String {
        val tz = TimeZone.getTimeZone("UTC")
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'")
        df.timeZone = tz
        return df.format(Date())
    }
}