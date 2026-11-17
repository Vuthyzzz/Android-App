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

    fun onGoogleAccountSelected(email: String) {
        state = state.copy(
            email = email,
            password = "",
            isGoogleLogin = true,
            message = "Google account selected. Continue with Google sign-in."
        )
    }

    fun onEvent(event: LoginEvent) {
        state = when (event) {
            is LoginEvent.EmailChanged -> state.copy(email = event.value, message = null)
            is LoginEvent.PasswordChanged -> state.copy(password = event.value, message = null)
            is LoginEvent.RememberMeChanged -> state.copy(rememberMe = event.value)
            LoginEvent.TogglePasswordVisibility ->
                state.copy(passwordVisible = !state.passwordVisible)
            LoginEvent.GoogleSignInClicked -> {
                val googleAccountEmail = if (state.email.isValidEmail()) state.email else "user.google@gmail.com"
                state.copy(
                    email = googleAccountEmail,
                    password = "",
                    isGoogleLogin = true,
                    message = "Starting Google sign-in..."
                )
            }
            LoginEvent.Submit -> {
                if (state.isGoogleLogin) {
                    if (state.email.isBlank()) {
                        state.copy(message = "Please enter a Google email address.")
                    } else {
                        state.copy(
                            isGoogleLogin = true,
                            requiresVerification = false,
                            message = "Google sign-in will continue through Google authentication."
                        )
                    }
                } else {
                    if (!state.email.isValidEmail()) {
                        state.copy(message = "Please enter a valid email address.")
                    } else if (state.password.length < 8) {
                        state.copy(message = "Please use a password with at least 8 characters.")
                    } else {
                        auth.signInWithEmailAndPassword(state.email, state.password)
                            .addOnSuccessListener {
                                state = state.copy(
                                    isLoggedIn = true,
                                    requiresVerification = false,
                                    isGoogleLogin = false,
                                    message = null
                                )
                            }
                            .addOnFailureListener { e ->
                                state = state.copy(
                                    message = e.localizedMessage ?: "Sign in failed.",
                                    isLoggedIn = false,
                                    requiresVerification = false,
                                    isGoogleLogin = false
                                )
                            }
                        state.copy(
                            requiresVerification = false,
                            isGoogleLogin = false,
                            message = null
                        )
                    }
                }
            }
            LoginEvent.ClearVerificationPending -> state.copy(
                requiresVerification = false,
                isLoggedIn = false,
                isGoogleLogin = false,
                message = null
            )
        }
    }

    fun completeLoginAfterVerification() {
        state = state.copy(isLoggedIn = true, requiresVerification = false, isGoogleLogin = false)
    }
}

internal fun String.isValidEmail(): Boolean =
    contains("@") && substringAfter("@").contains(".")
