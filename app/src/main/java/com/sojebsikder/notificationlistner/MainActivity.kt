package com.sojebsikder.notificationlistner

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sojebsikder.notificationlistner.ui.theme.NotificationListenerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }

        setContent {
            NotificationListenerTheme {
                NotificationLoggerApp(
                    onSendTestNotification = { sendTestNotification() }
                )
            }
        }
    }

    private fun sendTestNotification() {
        val channelId = "test_channel"
        val channelName = "Test Channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create Notification Channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Create and send the notification
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Test Notification")
            .setContentText("This is a test notification.")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        notificationManager.notify(1, notification)
    }
}

@Composable
fun NotificationLoggerApp(
    viewModel: NotificationViewModel = viewModel(),
    onSendTestNotification: () -> Unit
) {
    val notifications by viewModel.notifications.collectAsState()
    val context = LocalContext.current
    var selectedNotification by remember { mutableStateOf<String?>(null) }

    Scaffold(
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    }
                ) {
                    Text("Grant Notification Access")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.clearNotifications() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Clear Notifications")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onSendTestNotification,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Send Test Notification")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Received Notifications:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                if (notifications.isEmpty()) {
                    Text("No notifications received yet.")
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(notifications.reversed()) { notification ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedNotification = notification }
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = notification,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                HorizontalDivider()
                            }
                        }
                    }
                }

                if (selectedNotification != null) {
                    AlertDialog(
                        onDismissRequest = { selectedNotification = null },
                        title = { Text("Notification Details") },
                        text = { Text(selectedNotification ?: "") },
                        confirmButton = {
                            TextButton(onClick = { selectedNotification = null }) {
                                Text("Close")
                            }
                        }
                    )
                }
            }
        }
    )
}
