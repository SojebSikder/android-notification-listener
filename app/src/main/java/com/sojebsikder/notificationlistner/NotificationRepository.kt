package com.sojebsikder.notificationlistner

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object NotificationRepository {
    private val _notifications = MutableStateFlow<List<String>>(emptyList())
    val notifications = _notifications.asStateFlow()

    fun addNotification(text: String) {
        _notifications.value = _notifications.value + text
    }

    fun clear() {
        _notifications.value = emptyList()
    }
}