
package com.example.alarmageddon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CrewBackground)
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
                text = "3 members",
                fontSize = 13.sp,
                color = CrewMuted
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        CrewMemberCard(
            initials = "K",
            name = "Kylie",
            streak = "5 day streak",
            progress = 1.0f,
            isYou = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        CrewMemberCard(
            initials = "A",
            name = "Alex",
            streak = "4 day streak",
            progress = 0.8f
        )

        Spacer(modifier = Modifier.height(12.dp))

        CrewMemberCard(
            initials = "J",
            name = "Jordan",
            streak = "3 day streak",
            progress = 0.6f
        )

        Spacer(modifier = Modifier.weight(1f))

        // Invite button
        Card(
            modifier = Modifier.fillMaxWidth(),
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
    streak: String,
    progress: Float,
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

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = streak,
                    fontSize = 12.sp,
                    color = CrewMuted
                )

                Spacer(modifier = Modifier.height(9.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp),
                    color = Color(0xFF8065E8),
                    trackColor = CrewLavender
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "${(progress * 100).toInt()}%",
                fontSize = 12.sp,
                color = CrewMuted
            )
        }
    }
}
