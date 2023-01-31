package com.commer.app.ui

import java.text.SimpleDateFormat
import java.util.*

object TimeAgo {

    private const val SECOND = 1
    private const val MINUTE = 60 * SECOND
    private const val HOUR = 60 * MINUTE
    private const val DAY = 24 * HOUR
    private const val MONTH = 30 * DAY
    private const val YEAR = 12 * MONTH

    private fun currentDate(): Long {
        val calendar = Calendar.getInstance()
        return calendar.timeInMillis
    }

    // Long: time in millisecond
    fun Date.toTimeAgo(): String {
        val time = this.time
        val now = currentDate()

        // convert back to second
        val diff = (now - time) / 1000

        return when {
            diff < MINUTE -> "just now"
            diff < 2 * MINUTE -> "1m ago"
            diff < 60 * MINUTE -> "${diff / MINUTE}m ago"
            diff < 2 * HOUR -> "1h ago"
            diff < 24 * HOUR -> "${diff / HOUR}h ago"
            diff < 2 * DAY -> "yesterday"
            diff < 30 * DAY -> "${diff / DAY}d ago"
            diff < 2 * MONTH -> "1mo ago"
            diff < 12 * MONTH -> "${diff / MONTH}mo ago"
            diff < 2 * YEAR -> "1y ago"
            else -> "${diff / YEAR}y ago"
        }
    }

    fun Date.toCustomDate(): String {
        val convert = SimpleDateFormat("MMMM dd yyyy, hh:mm a", Locale.getDefault())
        return convert.format(this)
    }
}