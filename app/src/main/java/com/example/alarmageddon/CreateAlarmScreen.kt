
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
private val CreateLavender = Color(0xFFF0EEFF)
private val CreateBackground = Color(0xFFF9F8FF)

@Composable
fun CreateAlarmScreen(
    onBack: () -> Unit,
    onCreate: (Alarm) -> Unit
) {
    val context = LocalContext.current

    var selectedTime by remember {
        mutableStateOf("7:00 AM")
    }

    val weekdays = listOf(
        "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
    )

    var selectedDays by remember {
        mutableStateOf(setOf("Mon", "Tue", "Wed", "Thu", "Fri"))
    }

    // Demo friends - replace with Firebase data later
    val availableFriends = listOf(
        "Alex Morgan",
        "Jordan Lee",
        "Sam Chen"
    )

    val selectedFriends = remember {
        mutableStateListOf<String>()
    }

    var searchQuery by remember {
        mutableStateOf("")
    }

    var showSearch by remember {
        mutableStateOf(false)
    }

    val stakes = listOf(
        "Buy Coffee",
        "Buy Breakfast",
        "Pay $5",
        "Run 3km",
        "Post Weird Selfie",
        "Custom"
    )

    var selectedStake by remember {
        mutableStateOf(stakes[0])
    }

    var customStake by remember {
        mutableStateOf("")
    }

    val filteredFriends = availableFriends.filter {
        it.contains(searchQuery, ignoreCase = true) &&
                it !in selectedFriends
    }

    val finalStake = if (selectedStake.contains("Custom")) {
        customStake.trim()
    } else {
        selectedStake
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

                        val hour = formatted.get(Calendar.HOUR).let {
                            if (it == 0) 12 else it
                        }

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
                containerColor = CreateLavender
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
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            weekdays.forEach { day ->
                val isSelected = day in selectedDays

                FilterChip(
                    selected = isSelected,
                    onClick = {
                        selectedDays = if (isSelected) {
                            selectedDays - day
                        } else {
                            selectedDays + day
                        }
                    },
                    modifier = Modifier.weight(1f),
                    label = {
                        Text(
                            text = day.take(1),
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Crew selection
        Text(
            text = "YOUR CREW",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "👥  Add your friends",
                fontSize = 19.sp,
                fontWeight = FontWeight.SemiBold,
                color = CreatePurple
            )

            OutlinedButton(
                onClick = {
                    showSearch = !showSearch
                    if (!showSearch) searchQuery = ""
                },
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = if (showSearch) "Close" else "⌕  Search",
                    color = CreatePurple
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Choose who joins this alarm pact.",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Selected friends
        if (selectedFriends.isNotEmpty()) {
            Text(
                text = "SELECTED FRIENDS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(6.dp))

            selectedFriends.toList().forEach { friend ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "👤  $friend",
                        color = CreatePurple,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(10.dp)
                    )

                    OutlinedButton(
                        onClick = {
                            selectedFriends.remove(friend)
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("×", color = CreatePurple)
                    }
                }
            }
        } else {
            Text(
                text = "No friends selected yet.",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search field and friend results
        if (showSearch) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                placeholder = {
                    Text("Search friends...")
                },
                leadingIcon = {
                    Text("⌕", fontSize = 22.sp)
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (filteredFriends.isEmpty()) {
                Text(
                    text = "No friends found.",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            } else {
                filteredFriends.forEach { friend ->
                    OutlinedButton(
                        onClick = {
                            selectedFriends.add(friend)
                            searchQuery = ""
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "👤  $friend",
                                color = CreatePurple
                            )
                            Text(
                                text = "+ Add",
                                color = CreatePurple
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "${selectedFriends.size + 1} people in this pact (including you)",
            fontSize = 13.sp,
            color = CreatePurple,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Stakes
        Text(
            text = "WHAT'S AT STAKE?",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Choose what happens if you miss your alarm.",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

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
                    .padding(bottom = 7.dp)
            )
        }

        // Custom stake input
        if (selectedStake.contains("Custom")) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "YOUR OWN CONSEQUENCE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = customStake,
                onValueChange = { customStake = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                placeholder = {
                    Text("e.g. Buy everyone lunch...")
                },
                label = {
                    Text("Custom consequence")
                },
                minLines = 2,
                maxLines = 4
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Create button
        Button(
            onClick = {
                val newAlarm = Alarm(
                        time = selectedTime,
                repeatDays = weekdays.filter {
                    it in selectedDays
                },
                crewCount = selectedFriends.size + 1,
                stake = finalStake,
                crewMembers = selectedFriends.toList()
                )

                onCreate(newAlarm)
            },
            enabled = selectedDays.isNotEmpty() &&
                    finalStake.isNotBlank(),
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
