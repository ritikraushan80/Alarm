package com.example.alarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.TimePicker
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class MainActivity : AppCompatActivity() {
    var alarmTimePicker: TimePicker? = null
    var pendingIntent: PendingIntent? = null
    var alarmManager: AlarmManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotification()

        alarmTimePicker = findViewById<View>(R.id.timePicker) as TimePicker
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
    }


    fun OnToggleClicked(view: View) {
        var time: Long
        if ((view as Switch).isChecked) {
            Toast.makeText(this, "ALARM ON", Toast.LENGTH_SHORT).show()
            val calendar = Calendar.getInstance()

            calendar[Calendar.HOUR_OF_DAY] = alarmTimePicker!!.currentHour
            calendar[Calendar.MINUTE] = alarmTimePicker!!.currentMinute

            val intent = Intent(this, AlarmReceiver::class.java)


            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
            time = calendar.timeInMillis - calendar.timeInMillis % 60000
            if (System.currentTimeMillis() > time) {

                time =
                    if (Calendar.AM_PM == 0) time + 1000 * 60 * 60 * 12 else time + 1000 * 60 * 60 * 24
            }

            alarmManager!!.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, pendingIntent)
        } else {
            alarmManager!!.cancel(pendingIntent)
            Toast.makeText(this, "ALARM OFF", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name: CharSequence = "AlarmReminder"
            val description = "Mobile Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mobile = NotificationChannel("AlarmApp", name,importance)
            mobile.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)

            notificationManager.createNotificationChannel(mobile)

        }
    }
}
