package com.sojebsikder.notificationlistner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sojebsikder.notificationlistner.ui.theme.NotificationListenerTheme

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotificationListenerTheme {
                NotificationLoggerApp()
            }
        }
    }
}

@Composable
fun NotificationLoggerApp(viewModel: NotificationViewModel = viewModel()) {
    val notifications by viewModel.notifications.collectAsState()
    val context = LocalContext.current

    Scaffold(
        content = { padding ->
            Column(modifier = Modifier
                .padding(padding)
                .padding(16.dp)) {

                Button(onClick = {
                    val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }) {
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

                Text("Received Notifications:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                if (notifications.isEmpty()) {
                    Text("No notifications received yet.")
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(notifications.reversed()) { notification ->
                            Text(
                                text = notification,
                                modifier = Modifier.padding(8.dp),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    )
}