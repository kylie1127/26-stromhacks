
package com.example.alarmageddon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Lavender = Color(0xFFB9AEFF)
private val LightLavender = Color(0xFFF0EEFF)
private val DarkPurple = Color(0xFF343052)
private val SoftYellow = Color(0xFFFFC76A)
private val BackgroundColor = Color(0xFFF9F8FF)
private val TextGray = Color(0xFF858397)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {

                val alarms = remember {
                    mutableStateListOf<Alarm>()
                }

                var showCreateScreen by remember {
                    mutableStateOf(false)
                }

                if (showCreateScreen) {
                    CreateAlarmScreen(
                        onBack = {
                            showCreateScreen = false
                        },
                        onCreate = { newAlarm ->
                            alarms.add(newAlarm)
                            showCreateScreen = false
                        }
                    )
                } else {
                    AlarmageddonHome(
                        alarms = alarms,
                        onAddAlarm = {
                            showCreateScreen = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AlarmageddonHome(
    alarms: List<Alarm>,
    onAddAlarm: () -> Unit
) {
    Scaffold(
        containerColor = BackgroundColor,
        bottomBar = {
            BottomNavigation()
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp)
                .padding(top = 28.dp, bottom = 24.dp)
        ) {

            Text(
                text = "Good morning, Kylie ☀️",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = DarkPurple
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Ready to conquer your morning?",
                fontSize = 14.sp,
                color = TextGray
            )

            Spacer(modifier = Modifier.height(28.dp))

            SectionTitle("NEXT WAKE-UP")

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = LightLavender
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                ) {
                    Text(
                        text = "🌙  Tomorrow morning",
                        fontSize = 14.sp,
                        color = DarkPurple
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = alarms.firstOrNull()?.time ?: "7:00 AM",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkPurple
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Your crew is counting on you!",
                        fontSize = 13.sp,
                        color = DarkPurple
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("👩🏻", fontSize = 22.sp)
                        Text(" 👨🏻", fontSize = 22.sp)
                        Text(" 👩🏽", fontSize = 22.sp)

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "${alarms.firstOrNull()?.crewCount ?: 3} people in this pact",
                            fontSize = 12.sp,
                            color = DarkPurple
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            SectionTitle("MY PACT")

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "🔥 Best Streak",
                    value = "12 days",
                    background = SoftYellow,
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    title = "☀️ Average Wake-Up",
                    value = "6:42 AM",
                    background = Lavender,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionTitle("YOUR CREW")

                Text(
                    text = "See all →",
                    fontSize = 13.sp,
                    color = DarkPurple
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            CrewCard(
                emoji = "👩🏻",
                name = "Sarah Johnson",
                status = "Ready for tomorrow!"
            )

            Spacer(modifier = Modifier.height(10.dp))

            CrewCard(
                emoji = "👨🏻",
                name = "Alex Chen",
                status = "Last check-in: 7:00 AM"
            )

            Spacer(modifier = Modifier.height(28.dp))

            SectionTitle("MY ALARMS")

            Spacer(modifier = Modifier.height(10.dp))

            AlarmCard(
                time = "7:00 AM",
                title = "Morning Workout",
                days = "Mon · Tue · Wed · Thu · Fri",
                stake = "Buy Coffee ☕"
            )

            Spacer(modifier = Modifier.height(10.dp))

            AlarmCard(
                time = "8:30 AM",
                title = "Weekend Study",
                days = "Sat · Sun",
                stake = "Buy Breakfast 🍳"
            )

            alarms.forEach { alarm ->
                Spacer(modifier = Modifier.height(10.dp))

                AlarmCard(
                    time = alarm.time,
                    title = "My New Pact",
                    days = if (alarm.repeatDays.isEmpty()) {
                        "No repeat"
                    } else {
                        alarm.repeatDays.joinToString(" · ")
                    },
                    stake = "${alarm.stake} · ${alarm.crewCount} people"
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = onAddAlarm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkPurple
                )
            ) {
                Text(
                    text = "+  Add New Alarm",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = TextGray,
        letterSpacing = 1.sp
    )
}

@Composable
fun StatCard(
    title: String,
    value: String,
    background: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = background
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                color = DarkPurple
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = value,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = DarkPurple
            )
        }
    }
}

@Composable
fun CrewCard(
    emoji: String,
    name: String,
    status: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = emoji,
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkPurple
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = status,
                    fontSize = 12.sp,
                    color = TextGray
                )
            }

            Text(
                text = "●",
                color = Color(0xFF69BE86),
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun AlarmCard(
    time: String,
    title: String,
    days: String,
    stake: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = time,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkPurple
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = DarkPurple
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = days,
                    fontSize = 11.sp,
                    color = TextGray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stake,
                    fontSize = 11.sp,
                    color = DarkPurple
                )
            }

            Text(
                text = "🔔",
                fontSize = 25.sp
            )
        }
    }
}

@Composable
fun BottomNavigation() {
    Surface(
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem("⌂", "Home", true)
            BottomNavItem("◷", "Pacts", false)
            BottomNavItem("♧", "Crew", false)
            BottomNavItem("☺", "Profile", false)
        }
    }
}

@Composable
fun BottomNavItem(
    icon: String,
    label: String,
    selected: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            fontSize = 22.sp,
            color = if (selected) DarkPurple else TextGray
        )

        Text(
            text = label,
            fontSize = 11.sp,
            color = if (selected) DarkPurple else TextGray,
            fontWeight = if (selected) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            }
        )
    }
}
