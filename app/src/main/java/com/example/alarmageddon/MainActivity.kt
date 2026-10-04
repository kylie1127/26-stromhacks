package com.example.alarmageddon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.Path
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private val Purple = Color(0xFF343052)
private val Lavender = Color(0xFFF0EEFF)
private val Background = Color(0xFFFAF9FF)
private val Muted = Color(0xFF89869D)


enum class MainTab {
    HOME, CREW, ALARMS, PROFILE
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                AlarmageddonApp()
            }
        }
    }
}

@Composable
fun AlarmageddonApp() {
    var signedIn by remember {
        mutableStateOf(FirebaseAuth.getInstance().currentUser != null)
    }

    if (!signedIn) {
        AuthScreen(
            onSuccess = { signedIn = true }
        )
        return
    }

    val alarms = remember { mutableStateListOf<Alarm>() }

    var selectedTab by remember { mutableStateOf(MainTab.HOME) }
    var showCreateScreen by remember { mutableStateOf(false) }
    var selectedAlarm by remember { mutableStateOf<Alarm?>(null) }
    var editingAlarm by remember { mutableStateOf<Alarm?>(null) }

    when {
        selectedAlarm != null -> {
            AlarmDetailScreen(
                alarm = selectedAlarm!!,
                onBack = {
                    selectedAlarm = null
                },
                onEdit = { alarm ->
                    editingAlarm = alarm
                    selectedAlarm = null
                },
                onDelete = { alarm ->
                    alarms.removeAll { it.id == alarm.id }
                    selectedAlarm = null
                }
            )
        }

        editingAlarm != null -> {
            CreateAlarmScreen(
                initialAlarm = editingAlarm,
                onBack = {
                    editingAlarm = null
                },
                onCreate = { updatedAlarm ->
                    val index = alarms.indexOfFirst {
                        it.id == updatedAlarm.id
                    }

                    if (index != -1) {
                        alarms[index] = updatedAlarm
                    }

                    editingAlarm = null
                }
            )
        }

        showCreateScreen -> {
            CreateAlarmScreen(
                onBack = {
                    showCreateScreen = false
                },
                onCreate = { alarm ->
                    alarms.add(alarm)
                    showCreateScreen = false
                    selectedTab = MainTab.ALARMS
                }
            )
        }

        else -> {
            Scaffold(
                containerColor = Color(0xFFFAF9FF),
                bottomBar = {
                    AlarmBottomNavigation(
                        selectedTab = selectedTab,
                        onTabSelected = {
                            selectedTab = it
                        }
                    )
                }
            ) { innerPadding ->

                when (selectedTab) {
                    MainTab.HOME -> {
                        AlarmageddonHome(
                            alarms = alarms,
                            onAddAlarm = {
                                showCreateScreen = true
                            },
                            onAlarmClick = {
                                selectedAlarm = it
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }

                    MainTab.CREW -> {
                        CrewDetailScreen(
                            onBack = {
                                selectedTab = MainTab.HOME
                            }
                        )
                    }

                    MainTab.ALARMS -> {
                        AlarmListScreen(
                            alarms = alarms,
                            onAddAlarm = {
                                showCreateScreen = true
                            },
                            onAlarmClick = {
                                selectedAlarm = it
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }

                    MainTab.PROFILE -> {
                        ProfileScreen(
                            onSignOut = {
                                FirebaseAuth.getInstance().signOut()
                                signedIn = false
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
// 하단 네비게이션 바

@Composable
fun AlarmBottomNavigation(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Purple,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = selectedTab == MainTab.HOME,
            onClick = { onTabSelected(MainTab.HOME) },
            icon = {
                BottomBarIcon("home", selectedTab == MainTab.HOME)
            },
            label = { Text("Home", fontSize = 14.sp) },
            colors = navigationItemColors()
        )

        NavigationBarItem(
            selected = selectedTab == MainTab.CREW,
            onClick = { onTabSelected(MainTab.CREW) },
            icon = {
                BottomBarIcon("crew", selectedTab == MainTab.CREW)
            },
            label = { Text("Crew", fontSize = 14.sp) },
            colors = navigationItemColors()
        )

        NavigationBarItem(
            selected = selectedTab == MainTab.ALARMS,
            onClick = { onTabSelected(MainTab.ALARMS) },
            icon = {
                BottomBarIcon("alarm", selectedTab == MainTab.ALARMS)
            },
            label = { Text("Alarms", fontSize = 14.sp) },
            colors = navigationItemColors()
        )

        NavigationBarItem(
            selected = selectedTab == MainTab.PROFILE,
            onClick = { onTabSelected(MainTab.PROFILE) },
            icon = {
                BottomBarIcon("profile", selectedTab == MainTab.PROFILE)
            },
            label = { Text("Profile", fontSize = 14.sp) },
            colors = navigationItemColors()
        )
    }
}

@Composable
private fun BottomBarIcon(
    type: String,
    selected: Boolean
) {
    val iconColor = if (selected) {
        Color(0xFF8065E8)
    } else {
        Color(0xFF89869D)
    }

    Canvas(
        modifier = Modifier.size(28.dp)
    ) {
        val w = size.width
        val h = size.height

        val stroke = 2.4.dp.toPx()

        when (type) {
            "home" -> {
                val path = Path().apply {
                    moveTo(w * 0.12f, h * 0.45f)
                    lineTo(w * 0.5f, h * 0.12f)
                    lineTo(w * 0.88f, h * 0.45f)
                    moveTo(w * 0.22f, h * 0.4f)
                    lineTo(w * 0.22f, h * 0.9f)
                    lineTo(w * 0.78f, h * 0.9f)
                    lineTo(w * 0.78f, h * 0.4f)
                    moveTo(w * 0.42f, h * 0.9f)
                    lineTo(w * 0.42f, h * 0.62f)
                    lineTo(w * 0.58f, h * 0.62f)
                    lineTo(w * 0.58f, h * 0.9f)
                }

                drawPath(
                    path = path,
                    color = iconColor,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = stroke,
                        cap = androidx.compose.ui.graphics.StrokeCap.Round,
                        join = androidx.compose.ui.graphics.StrokeJoin.Round
                    )
                )
            }

            "crew" -> {
                // 왼쪽 사람
                drawCircle(
                    color = iconColor,
                    radius = w * 0.14f,
                    center = androidx.compose.ui.geometry.Offset(w * 0.3f, h * 0.3f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(stroke)
                )

                // 오른쪽 사람
                drawCircle(
                    color = iconColor,
                    radius = w * 0.14f,
                    center = androidx.compose.ui.geometry.Offset(w * 0.7f, h * 0.3f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(stroke)
                )

                // 몸통
                val path = Path().apply {
                    moveTo(w * 0.04f, h * 0.88f)
                    cubicTo(w * 0.04f, h * 0.55f, w * 0.56f, h * 0.55f, w * 0.56f, h * 0.88f)

                    moveTo(w * 0.48f, h * 0.65f)
                    cubicTo(w * 0.58f, h * 0.48f, w * 0.96f, h * 0.58f, w * 0.96f, h * 0.88f)
                }

                drawPath(
                    path,
                    iconColor,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = stroke,
                        cap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                )
            }

            "alarm" -> {
                // 시계 원
                drawCircle(
                    color = iconColor,
                    radius = w * 0.34f,
                    center = androidx.compose.ui.geometry.Offset(w * 0.5f, h * 0.53f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(stroke)
                )

                val path = Path().apply {
                    moveTo(w * 0.5f, h * 0.3f)
                    lineTo(w * 0.5f, h * 0.53f)
                    lineTo(w * 0.68f, h * 0.64f)

                    moveTo(w * 0.25f, h * 0.1f)
                    lineTo(w * 0.12f, h * 0.23f)

                    moveTo(w * 0.75f, h * 0.1f)
                    lineTo(w * 0.88f, h * 0.23f)
                }

                drawPath(
                    path,
                    iconColor,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = stroke,
                        cap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                )
            }

            "profile" -> {
                drawCircle(
                    color = iconColor,
                    radius = w * 0.2f,
                    center = androidx.compose.ui.geometry.Offset(w * 0.5f, h * 0.27f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(stroke)
                )

                val path = Path().apply {
                    moveTo(w * 0.16f, h * 0.9f)
                    cubicTo(w * 0.16f, h * 0.48f, w * 0.84f, h * 0.48f, w * 0.84f, h * 0.9f)
                }

                drawPath(
                    path,
                    iconColor,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = stroke,
                        cap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                )
            }
        }
    }
}

@Composable
private fun navigationItemColors() =
    NavigationBarItemDefaults.colors(
        selectedIconColor = Color(0xFF8065E8),
        selectedTextColor = Color(0xFF343052),
        indicatorColor = Color(0xFFE9DDFB),
        unselectedIconColor = Color(0xFF89869D),
        unselectedTextColor = Color(0xFF89869D)
    )


// Home 화면

@Composable
fun AlarmageddonHome(
    alarms: List<Alarm>,
    onAddAlarm: () -> Unit,
    onAlarmClick: (Alarm) -> Unit,
    modifier: Modifier = Modifier
) {
    var displayName by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@LaunchedEffect
        FirebaseFirestore.getInstance()
            .collection("users").document(uid)
            .get()
            .addOnSuccessListener { doc ->
                displayName = doc.getString("displayName") ?: ""
            }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "ALARMAGEDDON ⚡",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Purple
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (displayName.isNotBlank()) "Good morning, $displayName!" else "Good morning!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Purple
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Ready to keep your promises?",
            fontSize = 15.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(28.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Purple
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {

                Text(
                    text = "NEXT WAKE-UP",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (alarms.isNotEmpty()) alarms.last().time
                    else "07:00 AM",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Your crew is counting on you!",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            StatCard(
                title = "CURRENT STREAK",
                value = "5 days",
                modifier = Modifier.weight(1f)
            )

            StatCard(
                title = "YOUR CREW",
                value = "3 people",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Your Crew 👥",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = Purple
        )

        Spacer(modifier = Modifier.height(14.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Lavender
            )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text("🔥", fontSize = 28.sp)

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Morning Warriors",
                        fontWeight = FontWeight.Bold,
                        color = Purple
                    )

                    Text(
                        text = "Your crew is waiting for you",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }

                Text("›", fontSize = 26.sp, color = Purple)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "Your Alarms ⏰",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = Purple
            )

            Text(
                text = "${alarms.size} alarms",
                fontSize = 13.sp,
                color = Muted
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (alarms.isEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "No alarms yet!",
                        fontWeight = FontWeight.Bold,
                        color = Purple
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Create your first alarm pact.",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }
            }

        } else {

            alarms.forEach { alarm ->

                AlarmCard(
                    time = alarm.time,
                    title = "Your Alarm",
                    days = if (alarm.repeatDays.isEmpty()) {
                        "No repeat"
                    } else {
                        alarm.repeatDays.joinToString(" · ")
                    },
                    stake = alarm.stake,
                    onClick = { onAlarmClick(alarm) }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onAddAlarm,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Purple
            )
        ) {

            Text(
                text = "+  Add New Alarm",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// Crew 화면

@Composable
fun CrewScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "YOUR CREW",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Morning Warriors 🔥",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Purple
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Your crew is waiting for you.",
            fontSize = 15.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Lavender
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    "Your Crew",
                    fontWeight = FontWeight.Bold,
                    color = Purple
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "3 members",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

// Alarms 목록 화면

@Composable
fun AlarmListScreen(
    alarms: List<Alarm>,
    onAddAlarm: () -> Unit,
    onAlarmClick: (Alarm) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "YOUR ALARMS",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Stay on track.",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Purple
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (alarms.isEmpty()) {

            Text(
                text = "No alarms created yet.",
                color = Color.Gray,
                fontSize = 15.sp
            )

        } else {

            alarms.forEach { alarm ->

                AlarmCard(
                    time = alarm.time,
                    title = "Your Alarm",
                    days = if (alarm.repeatDays.isEmpty()) {
                        "No repeat"
                    } else {
                        alarm.repeatDays.joinToString(" · ")
                    },
                    stake = alarm.stake,
                    onClick = {
                        onAlarmClick(alarm)
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onAddAlarm,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Purple
            )
        ) {

            Text("+  Add New Alarm")
        }
    }
}

// Profile tab
@Composable
fun ProfileScreen(
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    var displayName by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@LaunchedEffect
        FirebaseFirestore.getInstance()
            .collection("users").document(uid)
            .get()
            .addOnSuccessListener { doc ->
                displayName = doc.getString("displayName") ?: ""
            }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "YOUR PROFILE",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            modifier = Modifier.size(76.dp),
            shape = RoundedCornerShape(38.dp),
            color = Lavender
        ) {

            Box(
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "K",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Purple
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (displayName.isNotBlank()) displayName else "Your profile",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Purple
        )

        Text(
            text = "Keep showing up for yourself.",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(28.dp))

        ProfileInfoCard(
            title = "Current streak",
            value = "5 days"
        )

        Spacer(modifier = Modifier.height(12.dp))

        ProfileInfoCard(
            title = "Crew membership",
            value = "Morning Warriors"
        )

        Spacer(modifier = Modifier.height(12.dp))
        ProfileInfoCard("Account", "Coming soon")

        OutlinedButton(
            onClick = onSignOut,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Sign out", color = Purple) }
    }
}

// Profile 정보 카드

@Composable
fun ProfileInfoCard(
    title: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Text(
                title,
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Purple
            )
        }
    }
}

// 공통 통계 카드

@Composable
fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Lavender
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Text(
                text = title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = Purple
            )
        }
    }
}

// 공통 알람 카드

@Composable
fun AlarmCard(
    time: String,
    title: String,
    days: String,
    stake: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = time,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Purple
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Purple
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = days,
                fontSize = 13.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "🔥 $stake",
                fontSize = 14.sp,
                color = Purple
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Tap to view details →",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}