package com.example.mobileappporject.ui.auth.forgotpassword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mobileappporject.ui.auth.login.isValidEmail
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordViewModel : ViewModel() {
    var state by mutableStateOf(ForgotPasswordState())
        private set

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun onEvent(event: ForgotPasswordEvent) {
        state = when (event) {
            is ForgotPasswordEvent.EmailChanged ->
                state.copy(email = event.value, message = null, isComplete = false)
            ForgotPasswordEvent.Submit -> {
                if (!state.email.isValidEmail()) {
                    state.copy(message = "Please enter a valid email address.", isComplete = false)
                } else {
                    auth.sendPasswordResetEmail(state.email)
                        .addOnSuccessListener {
                            state = state.copy(message = "Password reset email sent.", isComplete = true)
                        }
                        .addOnFailureListener { e ->
                            state = state.copy(message = e.localizedMessage ?: "Failed to send reset email.", isComplete = false)
                        }
                    state
                }
            }
            ForgotPasswordEvent.ClearCompletion -> state.copy(isComplete = false)
        }
    }
}
