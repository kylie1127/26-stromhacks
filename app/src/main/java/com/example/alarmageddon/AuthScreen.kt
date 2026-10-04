package com.example.alarmageddon

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

private val AuthPurple = Color(0xFF7052B5)
private val AuthLavender = Color(0xFFF0EAF9)
private val AuthBackground = Color(0xFFFAF8FD)
private val AuthMuted = Color(0xFF888397)

@Composable
fun AuthScreen(onSuccess: () -> Unit) {

    var isSignUp by remember { mutableStateOf(true) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuthBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(36.dp))

        // Brand logo
        Surface(
            modifier = Modifier.size(68.dp),
            shape = RoundedCornerShape(22.dp),
            color = AuthLavender
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "⚡",
                    fontSize = 34.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "ALARMAGEDDON",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            color = AuthPurple
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = if (isSignUp) {
                "Rise together."
            } else {
                "Welcome back."
            },
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF302A40),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isSignUp) {
                "Build better mornings with your crew."
            } else {
                "Your next great morning starts here."
            },
            fontSize = 14.sp,
            color = AuthMuted,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Authentication card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(22.dp)
            ) {

                // Sign up / Log in tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            AuthBackground,
                            RoundedCornerShape(16.dp)
                        )
                        .padding(4.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .background(
                                if (isSignUp) Color.White else Color.Transparent,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                isSignUp = true
                                status = ""
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Sign up",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSignUp) AuthPurple else AuthMuted
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .background(
                                if (!isSignUp) Color.White else Color.Transparent,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                isSignUp = false
                                status = ""
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Log in",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (!isSignUp) AuthPurple else AuthMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(26.dp))

                if (isSignUp) {

                    Text(
                        text = "YOUR NAME",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = AuthMuted
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("How should we call you?") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AuthPurple,
                            unfocusedBorderColor = Color(0xFFE6E1ED),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))
                }

                Text(
                    text = "EMAIL ADDRESS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = AuthMuted
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("you@example.com") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AuthPurple,
                        unfocusedBorderColor = Color(0xFFE6E1ED),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "PASSWORD",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = AuthMuted
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = {
                        Text(
                            if (isSignUp) "At least 6 characters"
                            else "Enter your password"
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AuthPurple,
                        unfocusedBorderColor = Color(0xFFE6E1ED),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Existing Firebase authentication logic
                Button(
                    enabled = !loading,
                    onClick = {

                        if (isSignUp && name.isBlank()) {
                            status = "Please enter a display name"
                            return@Button
                        }

                        loading = true
                        status = ""

                        if (isSignUp) {

                            auth.createUserWithEmailAndPassword(
                                email.trim(),
                                password
                            )
                                .addOnSuccessListener { result ->

                                    val uid = result.user!!.uid

                                    FirebaseFirestore.getInstance()
                                        .collection("users").document(uid)
                                        .set(
                                            mapOf(
                                                "displayName" to name.trim(),
                                                "email" to email.trim().lowercase(),
                                                "createdAt" to FieldValue.serverTimestamp()
                                            )
                                        )
                                        .addOnSuccessListener {
                                            loading = false
                                            onSuccess()
                                        }
                                        .addOnFailureListener {
                                            loading = false
                                            status = "Account created, but profile save failed: ${it.message}"
                                        }
                                }
                                .addOnFailureListener {
                                    loading = false
                                    status = "Sign up failed: ${it.message}"
                                }

                        } else {

                            auth.signInWithEmailAndPassword(
                                email.trim(),
                                password
                            )
                                .addOnSuccessListener {
                                    loading = false
                                    onSuccess()
                                }
                                .addOnFailureListener {
                                    loading = false
                                    status = "Login failed: ${it.message}"
                                }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AuthPurple,
                        disabledContainerColor = AuthPurple.copy(alpha = 0.5f)
                    )
                ) {

                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = if (isSignUp) "Create account" else "Log in",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (status.isNotBlank()) {

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = status,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "BETTER MORNINGS. STRONGER TOGETHER.",
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.sp,
            color = AuthMuted,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(36.dp))
    }
}