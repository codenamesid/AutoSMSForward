package com.codenamesid.applicationseries.smsforward

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color

class NotificationUtils(base: Context) : ContextWrapper(base) {

    private var mManager: NotificationManager? = null

    val manager: NotificationManager
        get() {
            if (mManager == null) {
                mManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            return mManager as NotificationManager
        }

    init {
        createChannels()
    }

    private fun createChannels() {

        // create android channel
        val serviceStatusChannel = NotificationChannel(SERVICE_STATUS_CHANNEL_ID,
                SERVICE_STATUS_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        // Sets whether notifications posted to this channel should display notification lights
        serviceStatusChannel.enableLights(true)
        // Sets whether notification posted to this channel should vibrate.
        serviceStatusChannel.enableVibration(true)
        // Sets the notification light color for notifications posted to this channel
        serviceStatusChannel.lightColor = Color.GREEN
        // Sets whether notifications posted to this channel appear on the lockscreen or not
        serviceStatusChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        manager.createNotificationChannel(serviceStatusChannel)

        val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.enableVibration(true)
        notificationChannel.lightColor = Color.GRAY
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        manager.createNotificationChannel(notificationChannel)
    }

    companion object {
        const val SERVICE_STATUS_CHANNEL_ID = "com.codenamesid.applicationseries.smsforward.service"
        const val NOTIFICATION_CHANNEL_ID = "com.codenamesid.applicationseries.smsforward.notification"
        const val SERVICE_STATUS_CHANNEL_NAME = "Service Status"
        const val NOTIFICATION_CHANNEL_NAME = "Notifications"
    }
}