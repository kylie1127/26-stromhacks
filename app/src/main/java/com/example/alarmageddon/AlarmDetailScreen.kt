
package com.example.alarmageddon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DetailPurple = Color(0xFF343052)
private val DetailBackground = Color(0xFFF9F8FF)

@Composable
fun AlarmDetailScreen(
    alarm: Alarm,
    onBack: () -> Unit,
    onEdit: (Alarm) -> Unit,
    onDelete: (Alarm) -> Unit
) {
    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text(
                    "Delete this alarm?",
                    fontWeight = FontWeight.Bold,
                    color = DetailPurple
                )
            },
            text = {
                Text(
                    "Are you sure you want to delete this alarm pact? This action cannot be undone."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDelete(alarm)
                    }
                ) {
                    Text(
                        "Delete",
                        color = Color(0xFFB3261E),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text("Cancel", color = DetailPurple)
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DetailBackground)
            .padding(24.dp)
    ) {
        TextButton(onClick = onBack) {
            Text(
                "← Back",
                color = DetailPurple,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

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

        Spacer(modifier = Modifier.height(28.dp))

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

        Spacer(modifier = Modifier.height(14.dp))

        DetailInfoCard(title = "👥 YOUR CREW") {
            Text(
                text = "${alarm.crewCount} people in this pact",
                color = DetailPurple,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            if (alarm.crewMembers.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                alarm.crewMembers.forEach { member ->
                    Text(
                        text = "• $member",
                        color = DetailPurple,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

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
            onClick = {
                onEdit(alarm)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DetailPurple
            )
        ) {
            Text(
                "✏️  Edit Alarm",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                showDeleteDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFFB3261E)
            )
        ) {
            Text(
                "🗑  Delete Alarm",
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
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            content()
        }
    }
}
