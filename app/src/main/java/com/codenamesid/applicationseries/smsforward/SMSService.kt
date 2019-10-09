package com.codenamesid.applicationseries.smsforward

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat


class SMSService : Service() {
    private var receiver: SMSReceiver? = null

    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        receiver = SMSReceiver()
        val filter = IntentFilter()
        filter.priority = Integer.MAX_VALUE
        registerReceiver(receiver, filter)
        // Initializes NotificationChannel.
        Log.i("SMSReceiver", "Registered..")
        val notificationBuilder = getAndroidChannelNotification("SMS Forward", "Running")
        NotificationUtils(this).manager.notify(101, notificationBuilder.build())

    }

    private fun getAndroidChannelNotification(title: String, body: String): NotificationCompat.Builder {
        val notificationIntent = Intent(applicationContext,MainActivity::class.java)
        notificationIntent.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP
        notificationIntent.flags=Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        return NotificationCompat.Builder(applicationContext, NotificationUtils.SERVICE_STATUS_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(false)
    }


    override fun onDestroy() {
        if (receiver != null) {
            Log.i("SMSReceiver", "Destroyed..")
            unregisterReceiver(receiver)
            receiver = null
        }
        val alarmMgr = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(this, SMSService::class.java)
        val pendingIntent = PendingIntent.getService(this, 0, i, 0)
        alarmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10, pendingIntent)
        super.onDestroy()
    }
}
