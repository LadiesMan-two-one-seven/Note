package com.asanagaev.note.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.asanagaev.note.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

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
        var now by remember { mutableLongStateOf(System.currentTimeMillis()) }

        LaunchedEffect(timestamp) {
            while (true) {
                now = System.currentTimeMillis()
                delay(60_000)
            }
        }

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