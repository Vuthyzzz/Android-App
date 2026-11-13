package com.example.mobileappporject.ui.auth.verification

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class VerificationViewModel : ViewModel() {
    var state by mutableStateOf(VerificationState())
        private set

    fun start(email: String, purpose: VerificationPurpose) {
        state = VerificationState(email = email, purpose = purpose)
    }

    fun onEvent(event: VerificationEvent) {
        state = when (event) {
            is VerificationEvent.CodeChanged -> {
                val digits = event.value.filter(Char::isDigit).take(6)
                state.copy(code = digits, message = null, isVerified = false)
            }
            VerificationEvent.Verify -> {
                val error = if (state.code.length == 6) null
                else "Please enter the 6-digit verification code."
                state.copy(
                    message = error ?: "Code verified (demo).",
                    isVerified = error == null
                )
            }
            VerificationEvent.Resend ->
                state.copy(code = "", message = "A new verification code was sent (demo).")
            VerificationEvent.ClearCompletion -> state.copy(isVerified = false)
        }
    }
}
