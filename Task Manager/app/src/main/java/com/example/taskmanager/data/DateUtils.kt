package com.example.taskmanager.data

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DateUtils {
    @RequiresApi(Build.VERSION_CODES.O)
    fun convertMillisToString(millis: Long): String {
        val instant = Instant.ofEpochMilli(millis)
        val zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("Z"))
        val formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
        return formatter.format(zonedDateTime)
    }
}