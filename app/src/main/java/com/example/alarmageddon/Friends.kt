package com.example.alarmageddon

import androidx.compose.runtime.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

data class Friend(val uid: String, val displayName: String, val email: String)

data class FriendRequest(
    val id: String,
    val fromUid: String,
    val fromName: String,
    val fromEmail: String
)

private val db get() = FirebaseFirestore.getInstance()

// Live list of my friends
@Composable
fun rememberFriends(): State<List<Friend>> {
    val state = remember { mutableStateOf<List<Friend>>(emptyList()) }
    DisposableEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val reg: ListenerRegistration? = uid?.let {
            db.collection("users").document(it).collection("friends")
                .addSnapshotListener { snap, _ ->
                    state.value = snap?.documents?.map { d ->
                        Friend(
                            d.id,
                            d.getString("displayName") ?: "",
                            d.getString("email") ?: ""
                        )
                    } ?: emptyList()
                }
        }
        onDispose { reg?.remove() }
    }
    return state
}

// Live list of pending requests sent to me
@Composable
fun rememberIncomingRequests(): State<List<FriendRequest>> {
    val state = remember { mutableStateOf<List<FriendRequest>>(emptyList()) }
    DisposableEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val reg: ListenerRegistration? = uid?.let {
            db.collection("friendRequests")
                .whereEqualTo("toUid", it)
                .whereEqualTo("status", "pending")
                .addSnapshotListener { snap, _ ->
                    state.value = snap?.documents?.map { d ->
                        FriendRequest(
                            d.id,
                            d.getString("fromUid") ?: "",
                            d.getString("fromName") ?: "",
                            d.getString("fromEmail") ?: ""
                        )
                    } ?: emptyList()
                }
        }
        onDispose { reg?.remove() }
    }
    return state
}

fun sendFriendRequest(email: String, onResult: (String) -> Unit) {
    val me = FirebaseAuth.getInstance().currentUser
        ?: return onResult("Not signed in")
    val target = email.trim().lowercase()
    if (target.isEmpty()) return onResult("Enter an email")

    db.collection("users").whereEqualTo("email", target).limit(1).get()
        .addOnSuccessListener { snap ->
            val other = snap.documents.firstOrNull()
                ?: return@addOnSuccessListener onResult("No user with that email")
            val toUid = other.id
            if (toUid == me.uid) return@addOnSuccessListener onResult("That's you!")

            db.collection("users").document(me.uid)
                .collection("friends").document(toUid).get()
                .addOnSuccessListener { f ->
                    if (f.exists()) return@addOnSuccessListener onResult("Already friends")

                    db.collection("users").document(me.uid).get()
                        .addOnSuccessListener { myDoc ->
                            val data = mapOf(
                                "fromUid" to me.uid,
                                "toUid" to toUid,
                                "fromName" to (myDoc.getString("displayName") ?: ""),
                                "fromEmail" to (me.email ?: ""),
                                "status" to "pending",
                                "createdAt" to FieldValue.serverTimestamp()
                            )
                            db.collection("friendRequests")
                                .document("${me.uid}_$toUid").set(data)
                                .addOnSuccessListener {
                                    onResult("Request sent to ${other.getString("displayName") ?: target}")
                                }
                                .addOnFailureListener {
                                    onResult("Couldn't send. Maybe you already sent one?")
                                }
                        }
                }
        }
        .addOnFailureListener { onResult("Search failed: ${it.message}") }
}

fun acceptFriendRequest(req: FriendRequest, onResult: (String) -> Unit = {}) {
    val me = FirebaseAuth.getInstance().currentUser ?: return
    db.collection("users").document(me.uid).get().addOnSuccessListener { myDoc ->
        val batch = db.batch()
        batch.update(db.collection("friendRequests").document(req.id), "status", "accepted")
        batch.set(
            db.collection("users").document(me.uid)
                .collection("friends").document(req.fromUid),
            mapOf("displayName" to req.fromName, "email" to req.fromEmail)
        )
        batch.set(
            db.collection("users").document(req.fromUid)
                .collection("friends").document(me.uid),
            mapOf(
                "displayName" to (myDoc.getString("displayName") ?: ""),
                "email" to (me.email ?: "")
            )
        )
        batch.commit()
            .addOnSuccessListener { onResult("You and ${req.fromName} are now friends") }
            .addOnFailureListener { onResult("Accept failed: ${it.message}") }
    }
}

fun declineFriendRequest(req: FriendRequest) {
    db.collection("friendRequests").document(req.id).delete()
}