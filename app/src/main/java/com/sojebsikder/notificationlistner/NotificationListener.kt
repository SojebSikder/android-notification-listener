package com.sojebsikder.notificationlistner

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class MyNotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn?.notification?.extras?.let { extras ->
            val title = extras.getString("android.title")
            val text = extras.getCharSequence("android.text")
            val packageName = sbn.packageName

            val message = "$packageName\n$title: $text"
            Log.d("NotifLogger", message)

            NotificationRepository.addNotification(message)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        // Handle if needed
    }
}