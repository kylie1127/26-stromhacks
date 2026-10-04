
package com.example.alarmageddon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DetailPurple = Color(0xFF343052)
private val DetailLavender = Color(0xFFF0EEFF)
private val DetailBackground = Color(0xFFF9F8FF)

@Composable
fun AlarmDetailScreen(
    alarm: Alarm,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DetailBackground)
            .padding(24.dp)
    ) {
        // Back button
        TextButton(onClick = onBack) {
            Text(
                text = "← Back",
                color = DetailPurple,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "YOUR PACT ⚡",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = alarm.time,
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = DetailPurple
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Your crew is counting on you!",
            fontSize = 15.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        DetailInfoCard(title = "📅 REPEAT") {
            Text(
                text = if (alarm.repeatDays.isEmpty()) {
                    "No repeat"
                } else {
                    alarm.repeatDays.joinToString(" · ")
                },
                color = DetailPurple,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        DetailInfoCard(title = "👥 YOUR CREW") {
            Text(
                text = "${alarm.crewCount} people in this pact",
                color = DetailPurple,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        DetailInfoCard(title = "🔥 WHAT'S AT STAKE?") {
            Text(
                text = alarm.stake,
                color = DetailPurple,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DetailPurple
            )
        ) {
            Text(
                text = "Back to Home",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DetailInfoCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text =title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            content()
        }
    }
}
