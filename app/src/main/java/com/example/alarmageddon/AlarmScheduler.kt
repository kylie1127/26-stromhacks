package com.example.alarmageddon

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.os.Build
import java.util.Calendar

class AlarmScheduler(context: Context) {

    private val appContext = context.applicationContext

    private val alarmManager =
        appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun schedule(alarm: Alarm): Boolean {

        val triggerTime = getNextAlarmTime(alarm)
        if (triggerTime == null) {
            Log.e("AlarmScheduler", "❌ Could not compute trigger time for ${alarm.time}")
            return false
        }

        val intent = Intent(appContext, AlarmReceiver::class.java).apply {
            putExtra("alarmId", alarm.id)
            putExtra("alarmTime", alarm.time)
            putStringArrayListExtra("repeatDays", ArrayList(alarm.repeatDays))
        }

        val pendingIntent = PendingIntent.getBroadcast(
            appContext,
            alarm.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Opens the app when the user taps the alarm icon in the system UI
        val showIntent = PendingIntent.getActivity(
            appContext,
            0,
            Intent(appContext, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(triggerTime.timeInMillis, showIntent),
            pendingIntent
        )

        Log.d("AlarmScheduler", "✅ Scheduled ${alarm.id} for ${triggerTime.time}")
        return true
    }

    fun cancel(alarm: Alarm) {
        val intent = Intent(appContext, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            appContext,
            alarm.id.hashCode(),
            intent,
            PendingIntent.FLAG_NO_CREATE or
                    PendingIntent.FLAG_IMMUTABLE
        )

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    private fun getNextAlarmTime(alarm: Alarm): Calendar? {

        return try {
            val timeParts = alarm.time.trim().split(Regex("\\s+"))
            val clockParts = timeParts[0].split(":")

            var hour = clockParts[0].toInt()
            val minute = clockParts[1].toInt()
            val amPm = timeParts[1].uppercase()

            if (amPm == "PM" && hour != 12) {
                hour += 12
            } else if (amPm == "AM" && hour == 12) {
                hour = 0
            }

            val now = Calendar.getInstance()

            for (dayOffset in 0..7) {

                val candidate = Calendar.getInstance().apply {
                    timeInMillis = now.timeInMillis
                    add(Calendar.DAY_OF_YEAR, dayOffset)
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                val dayName = when (
                    candidate.get(Calendar.DAY_OF_WEEK)
                ) {
                    Calendar.MONDAY -> "Mon"
                    Calendar.TUESDAY -> "Tue"
                    Calendar.WEDNESDAY -> "Wed"
                    Calendar.THURSDAY -> "Thu"
                    Calendar.FRIDAY -> "Fri"
                    Calendar.SATURDAY -> "Sat"
                    Calendar.SUNDAY -> "Sun"
                    else -> ""
                }

                val dayIsSelected =
                    alarm.repeatDays.isEmpty() ||
                            dayName in alarm.repeatDays

                if (dayIsSelected && candidate.after(now)) {
                    return candidate
                }
            }

            null

        } catch (e: Exception) {
            null
        }
    }
}