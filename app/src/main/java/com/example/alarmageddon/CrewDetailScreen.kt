
package com.example.alarmageddon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val CrewPurple = Color(0xFF343052)
private val CrewLavender = Color(0xFFF0EEFF)
private val CrewBackground = Color(0xFFFAF9FF)
private val CrewMuted = Color(0xFF89869D)

@Composable
fun CrewDetailScreen(
    onBack: () -> Unit
) {
    val requests by rememberIncomingRequests()
    val friends by rememberFriends()

    var showAddDialog by remember { mutableStateOf(false) }
    var addEmail by remember { mutableStateOf("") }
    var addStatus by remember { mutableStateOf("") }
    var actionMessage by remember { mutableStateOf("") }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false; addStatus = "" },
            title = { Text("Add a friend") },
            text = {
                Column {
                    OutlinedTextField(
                        value = addEmail,
                        onValueChange = { addEmail = it },
                        label = { Text("Friend's email") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                    if (addStatus.isNotEmpty()) {
                        Spacer(Modifier.height(8.dp))
                        Text(addStatus, fontSize = 13.sp, color = CrewMuted)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    addStatus = "Searching..."
                    sendFriendRequest(addEmail) { addStatus = it }
                }) { Text("Send request") }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false; addStatus = "" }) {
                    Text("Close")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CrewBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) {
                Text(
                    text = "←",
                    fontSize = 25.sp,
                    color = CrewPurple
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "⚙",
                fontSize = 22.sp,
                color = CrewPurple
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "YOUR CREW",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = CrewMuted,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Morning Warriors",
            fontSize = 29.sp,
            fontWeight = FontWeight.Bold,
            color = CrewPurple
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Wake up together. Win together.",
            fontSize = 15.sp,
            color = CrewMuted
        )

        Spacer(modifier = Modifier.height(26.dp))

        // Weekly progress card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = CrewPurple
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "THIS WEEK'S PROGRESS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.7f),
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "85%",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "weekly success rate",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { 0.85f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(9.dp),
                    color = Color(0xFFB9A9FF),
                    trackColor = Color.White.copy(alpha = 0.2f)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Your crew is doing amazing this week.",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        if (requests.isNotEmpty()) {
            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Friend Requests",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = CrewPurple
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${requests.size} new",
                    fontSize = 13.sp,
                    color = CrewMuted
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            requests.forEach { req ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = req.fromName.ifBlank { "Someone" },
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = CrewPurple
                            )
                            Text(
                                text = req.fromEmail,
                                fontSize = 12.sp,
                                color = CrewMuted
                            )
                        }
                        Button(
                            onClick = { acceptFriendRequest(req) { actionMessage = it } },
                            contentPadding = PaddingValues(horizontal = 12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CrewPurple)
                        ) { Text("Accept", fontSize = 12.sp) }
                        TextButton(
                            onClick = { declineFriendRequest(req) },
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) { Text("Decline", fontSize = 12.sp, color = CrewMuted) }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (actionMessage.isNotEmpty()) {
                Text(actionMessage, fontSize = 12.sp, color = CrewMuted)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Crew Members",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = CrewPurple
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "${friends.size} members",
                fontSize = 13.sp,
                color = CrewMuted
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (friends.isEmpty()) {
            Text(
                text = "No friends yet. Add someone by email below.",
                fontSize = 13.sp,
                color = CrewMuted
            )
        } else {
            friends.forEach { friend ->
                CrewMemberCard(
                    initials = friend.displayName.firstOrNull()?.uppercase() ?: "?",
                    name = friend.displayName,
                    streak = friend.email
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        // Invite button
        Card(
            modifier = Modifier.fillMaxWidth()
                .clickable {
                    addEmail = ""
                    addStatus = ""
                    showAddDialog = true
                },
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = CrewLavender
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(
                            color = Color(0xFFDCD4FF),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        fontSize = 27.sp,
                        color = CrewPurple
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Invite a Friend",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = CrewPurple
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Grow your crew and stay accountable!",
                        fontSize = 11.sp,
                        color = CrewMuted
                    )
                }

                Text(
                    text = "›",
                    fontSize = 28.sp,
                    color = CrewPurple
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun CrewMemberCard(
    initials: String,
    name: String,
    streak: String? = null,
    progress: Float? = null,
    isYou: Boolean = false
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
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = CrewLavender,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = CrewPurple
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CrewPurple
                    )

                    if (isYou) {
                        Spacer(modifier = Modifier.width(7.dp))

                        Surface(
                            color = CrewLavender,
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = "YOU",
                                modifier = Modifier.padding(
                                    horizontal = 9.dp,
                                    vertical = 3.dp
                                ),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = CrewPurple
                            )
                        }
                    }
                }

                if (streak != null) {
                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = streak,
                        fontSize = 12.sp,
                        color = CrewMuted
                    )
                }

                if (progress != null) {
                    Spacer(modifier = Modifier.height(9.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp),
                        color = Color(0xFF8065E8),
                        trackColor = CrewLavender
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "${(progress * 100).toInt()}%",
                        fontSize = 12.sp,
                        color = CrewMuted
                    )
                }
            }
        }
    }
}
