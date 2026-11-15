package com.example.mobileappporject.ui.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {
    var state by mutableStateOf(LoginState())
        private set

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun onEvent(event: LoginEvent) {
        state = when (event) {
            is LoginEvent.EmailChanged -> state.copy(email = event.value, message = null)
            is LoginEvent.PasswordChanged -> state.copy(password = event.value, message = null)
            is LoginEvent.RememberMeChanged -> state.copy(rememberMe = event.value)
            LoginEvent.TogglePasswordVisibility ->
                state.copy(passwordVisible = !state.passwordVisible)
            LoginEvent.Submit -> {
                if (!state.email.isValidEmail()) {
                    state.copy(message = "Please enter a valid email address.")
                } else if (state.password.length < 8) {
                    state.copy(message = "Please use a password with at least 8 characters.")
                } else {
                    auth.signInWithEmailAndPassword(state.email, state.password)
                        .addOnSuccessListener {
                            state = state.copy(isLoggedIn = true, message = null)
                        }
                        .addOnFailureListener { e ->
                            state = state.copy(message = e.localizedMessage ?: "Sign in failed.")
                        }
                    state
                }
            }
        }
    }
}

internal fun String.isValidEmail(): Boolean =
    contains("@") && substringAfter("@").contains(".")
