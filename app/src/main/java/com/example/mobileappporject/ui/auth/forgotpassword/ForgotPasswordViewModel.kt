package com.example.mobileappporject.ui.auth.forgotpassword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mobileappporject.ui.auth.login.isValidEmail

class ForgotPasswordViewModel : ViewModel() {
    var state by mutableStateOf(ForgotPasswordState())
        private set

    fun onEvent(event: ForgotPasswordEvent) {
        state = when (event) {
            is ForgotPasswordEvent.EmailChanged ->
                state.copy(email = event.value, message = null, isComplete = false)
            ForgotPasswordEvent.Submit -> {
                val error = if (state.email.isValidEmail()) null
                else "Please enter a valid email address."
                state.copy(
                    message = error ?: "Verification code sent (demo).",
                    isComplete = error == null
                )
            }
            ForgotPasswordEvent.ClearCompletion -> state.copy(isComplete = false)
        }
    }
}
