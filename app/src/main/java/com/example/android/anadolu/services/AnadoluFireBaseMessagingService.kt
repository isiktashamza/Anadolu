package com.example.android.anadolu.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.example.android.anadolu.MainActivity
import com.example.android.anadolu.R
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.FirebaseMessagingService


class AnadoluFireBaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.from!!)
        Log.d(TAG, "Notification Message Body: " + remoteMessage.notification!!.body!!)

        val message = remoteMessage.notification!!.body


        val pendingIntent = NavDeepLinkBuilder(this)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.navigation_graph)
            .setDestination(R.id.PipeFragment)
            .createPendingIntent()

        val channelId = "default_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(message)
            .setColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setSound(defaultSoundUri)
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationBuilder.setCategory(Notification.CATEGORY_MESSAGE)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        } else {
            @Suppress("DEPRECATION")
            notificationBuilder.priority = Notification.PRIORITY_HIGH
        }
        val notification = notificationBuilder.build()
        notification.flags = Notification.FLAG_AUTO_CANCEL

        notificationManager.notify(1,notification)

    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        print("token: $token")
    }

    companion object {
        private val TAG = "FCM Service"
    }
}