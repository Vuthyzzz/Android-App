package com.example.mobileappporject.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class FirebaseAuthManager {

    private val auth = FirebaseAuth.getInstance()

    suspend fun register(
        email: String,
        password: String
    ): FirebaseUser? {

        val result = auth
            .createUserWithEmailAndPassword(email, password)
            .await()

        return result.user
    }

    suspend fun login(
        email: String,
        password: String
    ): FirebaseUser? {

        val result = auth
            .signInWithEmailAndPassword(email, password)
            .await()

        return result.user
    }

    fun logout() {
        auth.signOut()
    }

    fun currentUser(): FirebaseUser? {
        return auth.currentUser
    }
}