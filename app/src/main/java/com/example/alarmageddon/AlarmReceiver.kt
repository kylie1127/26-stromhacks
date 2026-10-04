package com.example.alarmageddon

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        // 알람이 울리도록 서비스 실행
        val serviceIntent = Intent(
            context,
            AlarmSoundService::class.java
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }

        // 반복 요일이 있다면 다음 알람도 예약
        val alarmId = intent.getStringExtra("alarmId") ?: return
        val alarmTime = intent.getStringExtra("alarmTime") ?: return
        val repeatDays = intent.getStringArrayListExtra("repeatDays")
            ?: arrayListOf()

        if (repeatDays.isNotEmpty()) {
            val alarm = Alarm(
                id = alarmId,
                time = alarmTime,
                repeatDays = repeatDays,
                crewCount = 1,
                stake = ""
            )

            AlarmScheduler(context).schedule(alarm)
        }
    }
}