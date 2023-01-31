package com.commer.app.ui

import android.content.Context
import androidx.core.app.NotificationCompat
import com.commer.app.R

class CustomNotification(context: Context, string: String): NotificationCompat.Builder(context, string) {
    init {
        setContentTitle("Upload to Server")
        setContentText("On progress")
        setSmallIcon(R.drawable.cloud_upload)
        priority = NotificationCompat.PRIORITY_DEFAULT
    }
}