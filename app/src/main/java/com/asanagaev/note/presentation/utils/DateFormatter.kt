package com.asanagaev.note.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.asanagaev.note.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

object DateFormatter {

    private val millisInHour = TimeUnit.HOURS.toMillis(1)
    private val millisInMinute = TimeUnit.MINUTES.toMillis(1)
    private val millisInDay = TimeUnit.DAYS.toMillis(1)
    private val formatter = SimpleDateFormat.getDateInstance(DateFormat.SHORT)

    fun formatCurrentDate(): String {
        return formatter.format(System.currentTimeMillis())
    }

    @Composable
    fun formatDateToString(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        return when {
            diff < millisInMinute -> stringResource(R.string.just_now)
            diff < millisInHour -> {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
                stringResource(R.string.minutes_ago, minutes)
            }

            diff < millisInDay -> {
                val hours = TimeUnit.MILLISECONDS.toHours(diff)
                stringResource(R.string.hours_ago, hours)
            }

            else -> {
                formatter.format(timestamp)
            }
        }
    }
}