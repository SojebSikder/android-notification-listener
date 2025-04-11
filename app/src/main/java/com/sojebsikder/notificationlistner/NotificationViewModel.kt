package com.sojebsikder.notificationlistner

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NotificationViewModel : ViewModel() {
    private val _notifications = MutableStateFlow<List<String>>(emptyList())
    val notifications: StateFlow<List<String>> = NotificationRepository.notifications

    fun addNotification(text: String) {
        _notifications.value = _notifications.value + text
    }

    fun clearNotifications() {
        NotificationRepository.clear()
    }
}