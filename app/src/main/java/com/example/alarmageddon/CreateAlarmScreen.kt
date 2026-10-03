
package com.example.alarmageddon

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar
import java.util.Locale

private val CreatePurple = Color(0xFF343052)
private val CreateLavender = Color(0xFFB9AEFF)
private val CreateBackground = Color(0xFFF9F8FF)

@Composable
fun CreateAlarmScreen(
    onBack: () -> Unit,
    onCreate: (Alarm) -> Unit
) {
    val context = LocalContext.current

    var selectedTime by remember { mutableStateOf("7:00 AM") }

    val weekdays = listOf(
        "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
    )

    var selectedDays by remember {
        mutableStateOf(setOf("Mon", "Tue", "Wed", "Thu", "Fri"))
    }

    var crewCount by remember { mutableStateOf(3) }

    val stakes = listOf(
        "Buy Coffee ☕",
        "Buy Breakfast 🍳",
        "Pay $5 💸",
        "No Social Media 📵"
    )

    var selectedStake by remember {
        mutableStateOf(stakes[0])
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreateBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 28.dp)
    ) {

        // Top bar

        OutlinedButton(
            onClick = onBack,
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("← Back")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Create your Pact ⚡",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            color = CreatePurple
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Make a promise. Keep each other accountable.",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Alarm time

        Text(
            text = "WAKE-UP TIME",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                val calendar = Calendar.getInstance()

                TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        val formatted = Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, hourOfDay)
                            set(Calendar.MINUTE, minute)
                        }

                        val hour = formatted.get(Calendar.HOUR)
                            .let { if (it == 0) 12 else it }

                        val amPm = if (
                            formatted.get(Calendar.AM_PM) == Calendar.AM
                        ) "AM" else "PM"

                        selectedTime = String.format(
                            Locale.getDefault(),
                            "%d:%02d %s",
                            hour,
                            minute,
                            amPm
                        )
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
                ).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(76.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF0EEFF)
            )
        ) {
            Text(
                text = "⏰  $selectedTime",
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                color = CreatePurple
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Repeat days

        Text(
            text = "REPEAT",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            weekdays.forEach { day ->
                FilterChip(
                    selected = day in selectedDays,
                    onClick = {
                        selectedDays = if (day in selectedDays) {
                            selectedDays - day
                        } else {
                            selectedDays + day
                        }
                    },
                    label = {
                        Text(day, fontSize = 11.sp)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Crew selection

        Text(
            text = "YOUR CREW",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "👥  $crewCount people in this pact",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = CreatePurple
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = {
                    if (crewCount > 1) crewCount--
                }
            ) {
                Text("−")
            }

            OutlinedButton(
                onClick = {
                    if (crewCount < 10) crewCount++
                }
            ) {
                Text("+")
            }
        }

        Text(
            text = "For now, this is a demo count. Friend invitations will come later.",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Stakes

        Text(
            text = "WHAT'S AT STAKE?",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(10.dp))

        stakes.forEach { stake ->
            FilterChip(
                selected = selectedStake == stake,
                onClick = {
                    selectedStake = stake
                },
                label = {
                    Text(stake, fontSize = 14.sp)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Create button

        Button(
            onClick = {
                val newAlarm = Alarm(
                    time = selectedTime,
                    repeatDays = weekdays.filter {
                        it in selectedDays
                    },
                    crewCount = crewCount,
                    stake = selectedStake
                )

                onCreate(newAlarm)
            },
            enabled = selectedDays.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CreatePurple
            )
        ) {
            Text(
                text = "Create Pact ⚡",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
