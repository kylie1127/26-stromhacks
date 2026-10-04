package com.example.alarmageddon

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

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
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TabRow(selectedTabIndex = if (isSignUp) 0 else 1) {
            Tab(selected = isSignUp, onClick = { isSignUp = true; status = "" },
                text = { Text("Sign up") })
            Tab(selected = !isSignUp, onClick = { isSignUp = false; status = "" },
                text = { Text("Log in") })
        }
        Spacer(Modifier.height(24.dp))

        if (isSignUp) {
            OutlinedTextField(
                value = name, onValueChange = { name = it },
                label = { Text("Display name") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
        }
        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Email") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text(if (isSignUp) "Password (6+ characters)" else "Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

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
                    auth.createUserWithEmailAndPassword(email.trim(), password)
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
                                .addOnSuccessListener { loading = false; onSuccess() }
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
                    auth.signInWithEmailAndPassword(email.trim(), password)
                        .addOnSuccessListener { loading = false; onSuccess() }
                        .addOnFailureListener {
                            loading = false
                            status = "Login failed: ${it.message}"
                        }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text(if (isSignUp) "Create account" else "Log in") }

        Spacer(Modifier.height(12.dp))
        Text(status, color = MaterialTheme.colorScheme.error)
    }
}