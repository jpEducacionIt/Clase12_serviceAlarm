package com.example.clase12_servicealarma

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var buttonTime: Button
    private lateinit var buttonSetAlarm: Button
    private lateinit var buttonCancelAlarm: Button
    private lateinit var textTime: TextView
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    private lateinit var picker: MaterialTimePicker
    private lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonTime = findViewById(R.id.buttonSelecTime)
        buttonSetAlarm = findViewById(R.id.buttonSetAlarm)
        buttonCancelAlarm = findViewById(R.id.buttonCancelAlarm)
        textTime = findViewById(R.id.textViewTime)

        buttonTime.setOnClickListener {
            showTimePicker()
        }

        buttonSetAlarm.setOnClickListener {
            setAlarm()
        }

        buttonCancelAlarm.setOnClickListener {
            cancelAlarm()
        }

        createNotificationChannel()
    }

    private fun cancelAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        alarmManager.cancel(pendingIntent)

        Toast.makeText(this, "Alarma cancelada", Toast.LENGTH_LONG).show()
    }

    private fun setAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        alarmManager.set(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        Toast.makeText(this, "Alarma seteada correctamente", Toast.LENGTH_LONG).show()
    }

    private fun showTimePicker() {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Educacion It Alarma")
            .build()

        picker.show(supportFragmentManager, "eduit")

        picker.addOnPositiveButtonClickListener {
            if(picker.hour > 12) {
                textTime.text = String.format("%02d", picker.hour -12) + " : " +String.format("%02d", picker.minute) + "PM"
            } else {
                textTime.text = String.format("%02d", picker.hour) + " : " +String.format("%02d", picker.minute) + "AM"
            }

            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
        }
    }

    private fun createNotificationChannel() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "educacionItChannel"
            val description = "canal de Alarma de EducacionIt"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("eduit", name, importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)

            notificationManager.createNotificationChannel(channel)
        }
    }
}