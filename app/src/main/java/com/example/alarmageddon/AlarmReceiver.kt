package com.example.alarmageddon

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        Log.d("AlarmReceiver", "🔥 ALARM RECEIVER FIRED")

        val alarmId = intent.getStringExtra("alarmId")
        val alarmTime = intent.getStringExtra("alarmTime")
        val repeatDays = intent.getStringArrayListExtra("repeatDays")
            ?: arrayListOf()

        Log.d(
            "AlarmReceiver",
            "alarmId=$alarmId alarmTime=$alarmTime repeatDays=$repeatDays"
        )

        try {
            val serviceIntent = Intent(
                context,
                AlarmSoundService::class.java
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d("AlarmReceiver", "Starting foreground service")
                context.startForegroundService(serviceIntent)
            } else {
                Log.d("AlarmReceiver", "Starting normal service")
                context.startService(serviceIntent)
            }

            Log.d("AlarmReceiver", "Service start requested")

        } catch (e: Exception) {
            Log.e(
                "AlarmReceiver",
                "❌ FAILED TO START ALARM SERVICE",
                e
            )
        }

        if (alarmId == null || alarmTime == null) {
            Log.e("AlarmReceiver", "❌ Missing alarm data")
            return
        }

        if (repeatDays.isNotEmpty()) {
            val alarm = Alarm(
                id = alarmId,
                time = alarmTime,
                repeatDays = repeatDays,
                crewCount = 1,
                stake = ""
            )

            Log.d("AlarmReceiver", "Scheduling next repeating alarm")

            AlarmScheduler(context).schedule(alarm)
        }
    }
}