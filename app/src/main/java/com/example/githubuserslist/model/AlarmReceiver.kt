package com.example.githubuserslist.model

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.githubuserslist.MainActivity
import com.example.githubuserslist.R
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

        companion object {
            const val TYPE_DAILY = "Daily Reminder"
            const val EXTRA_MESSAGE = "message"
            const val EXTRA_TYPE = "type"
            const val CHANNEL_ID = "Black13_"
            const val CHANNEL_NAME = "Daily Reminder"

            private const val ID_REMINDER = 100
            private const val TIME_DAILY = "09:00" // set the alarm time here
        }

        override fun onReceive(context: Context, intent: Intent) {
            val message = intent.getStringExtra(EXTRA_MESSAGE)
            showAlarmNotification(context, message)
        }

        fun setReminder(context: Context, type: String, message: String) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra(EXTRA_MESSAGE, message)
            intent.putExtra(EXTRA_TYPE, type)
            val timeArray =
                TIME_DAILY.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
            calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
            calendar.set(Calendar.SECOND, 0)

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                ID_REMINDER, intent, 0
            )
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
            Toast.makeText(context, "Reminder is Enable", Toast.LENGTH_SHORT).show()
        }

        fun unsetReminder(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            val requestCode = ID_REMINDER
            val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
            pendingIntent.cancel()
            alarmManager.cancel(pendingIntent)
            Toast.makeText(context, "Reminder is Disable", Toast.LENGTH_SHORT).show()
        }

        private fun showAlarmNotification(context: Context, message: String?) {

            val channelId = CHANNEL_ID
            val channelName = CHANNEL_NAME

            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(
                context, 0,
                intent, PendingIntent.FLAG_ONE_SHOT
            )

            val notificationManagerCompat =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_baseline_alarm_24)
                .setContentTitle("Daily Reminder")
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                builder.setChannelId(channelId)
                notificationManagerCompat.createNotificationChannel(channel)
            }

            val notification = builder.build()
            notificationManagerCompat.notify(ID_REMINDER, notification)
        }
}