package com.example.mobileappporject.ui.auth.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mobileappporject.ui.auth.login.isValidEmail

class RegisterViewModel : ViewModel() {
    var state by mutableStateOf(RegisterState())
        private set

    fun onEvent(event: RegisterEvent) {
        state = when (event) {
            is RegisterEvent.NameChanged ->
                state.copy(name = event.value, message = null, isComplete = false)
            is RegisterEvent.EmailChanged ->
                state.copy(email = event.value, message = null, isComplete = false)
            is RegisterEvent.PasswordChanged ->
                state.copy(password = event.value, message = null, isComplete = false)
            is RegisterEvent.ConfirmPasswordChanged ->
                state.copy(confirmPassword = event.value, message = null, isComplete = false)
            RegisterEvent.TogglePasswordVisibility ->
                state.copy(passwordVisible = !state.passwordVisible)
            RegisterEvent.Submit -> {
                val error = when {
                    state.name.isBlank() -> "Please enter your name."
                    !state.email.isValidEmail() -> "Please enter a valid email address."
                    state.password.length < 8 ->
                        "Please use a password with at least 8 characters."
                    state.password != state.confirmPassword ->
                        "Please make sure your passwords match."
                    else -> null
                }
                state.copy(
                    message = error ?: "Verification code sent (demo).",
                    isComplete = error == null
                )
            }
            RegisterEvent.ClearCompletion -> state.copy(isComplete = false)
        }
    }
}
