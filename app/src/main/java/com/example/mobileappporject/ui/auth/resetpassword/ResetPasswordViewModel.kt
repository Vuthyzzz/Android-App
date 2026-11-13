package com.example.mobileappporject.ui.auth.resetpassword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ResetPasswordViewModel : ViewModel() {
    var state by mutableStateOf(ResetPasswordState())
        private set

    fun onEvent(event: ResetPasswordEvent) {
        state = when (event) {
            is ResetPasswordEvent.Start -> ResetPasswordState(email = event.email)
            is ResetPasswordEvent.PasswordChanged ->
                state.copy(password = event.value, message = null, isComplete = false)
            is ResetPasswordEvent.ConfirmPasswordChanged ->
                state.copy(confirmPassword = event.value, message = null, isComplete = false)
            ResetPasswordEvent.TogglePasswordVisibility ->
                state.copy(passwordVisible = !state.passwordVisible)
            ResetPasswordEvent.Submit -> {
                val error = when {
                    state.password.length < 8 ->
                        "Please use a password with at least 8 characters."
                    state.password != state.confirmPassword ->
                        "Please make sure your passwords match."
                    else -> null
                }
                state.copy(
                    message = error
                        ?: "Password updated (demo). Sign in with your new password.",
                    isComplete = error == null
                )
            }
            ResetPasswordEvent.ClearCompletion -> state.copy(isComplete = false)
        }
    }
}
