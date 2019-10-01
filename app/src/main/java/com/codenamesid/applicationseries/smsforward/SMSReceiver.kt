package com.codenamesid.applicationseries.smsforward

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SMSReceiver : BroadcastReceiver() {
    internal var phoneNumber: String? = null
    var compositeDisposable = CompositeDisposable()
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            Log.i("SMSReceiver", "received..")
            val sharedPref = context.getSharedPreferences("store", Context.MODE_PRIVATE)
            phoneNumber = sharedPref.getString("PHONE_NUMBER", null)
            if (phoneNumber != null) {
                val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
                for (message in smsMessages) {
                    val smsText = message.displayMessageBody
                    val sms = SMS(message, phoneNumber!!, false)

                    //Log.i("SMSReceiver", "received message $smsText")
                    if (smsText.contains("Apple ID")) {
                        sendSMS(context, phoneNumber!!, message.displayMessageBody)
                        sms.relayed = true
                    }
                    compositeDisposable.add(Single.fromCallable {
                                SMSDB.getDatabase(context.applicationContext).getSMSDAO().insert(sms)
                            }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doFinally {
                                compositeDisposable.dispose()
                            }
                            .subscribe())
                }
            }
        } else if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            val serviceIntent = Intent(context, SMSService::class.java)
            context.startService(serviceIntent)
        }
    }

    private fun sendSMS(context: Context, phoneNo: String, msg: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNo, null, msg, null, null)
            Toast.makeText(context.applicationContext, "Message Sent",
                    Toast.LENGTH_LONG).show()
            val notificationBuilder = getAndroidChannelNotification(context, "SMS Forward", "Message: $msg Sent to $phoneNo")
            NotificationUtils(context).manager.notify(102, notificationBuilder.build())

        } catch (ex: Exception) {
            Toast.makeText(context.applicationContext, ex.message.toString(),
                    Toast.LENGTH_LONG).show()
            ex.printStackTrace()
        }

    }

    private fun getAndroidChannelNotification(context: Context, title: String, body: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, NotificationUtils.NOTIFICATION_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(false)
    }


}
