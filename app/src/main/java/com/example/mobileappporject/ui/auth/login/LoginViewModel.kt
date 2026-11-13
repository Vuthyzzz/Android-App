package com.example.mobileappporject.ui.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    var state by mutableStateOf(LoginState())
        private set

    fun onEvent(event: LoginEvent) {
        state = when (event) {
            is LoginEvent.EmailChanged -> state.copy(email = event.value, message = null)
            is LoginEvent.PasswordChanged -> state.copy(password = event.value, message = null)
            is LoginEvent.RememberMeChanged -> state.copy(rememberMe = event.value)
            LoginEvent.TogglePasswordVisibility ->
                state.copy(passwordVisible = !state.passwordVisible)
            LoginEvent.Submit -> {
                if (state.email.isValidEmail() && state.password.length >= 8) {
                    state.copy(isLoggedIn = true, message = null)
                } else {
                    state.copy(message = when {
                        !state.email.isValidEmail() -> "Please enter a valid email address."
                        else -> "Please use a password with at least 8 characters."
                    })
                }
            }
        }
    }
}

internal fun String.isValidEmail(): Boolean =
    contains("@") && substringAfter("@").contains(".")
