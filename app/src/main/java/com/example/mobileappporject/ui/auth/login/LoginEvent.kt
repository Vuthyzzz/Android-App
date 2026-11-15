package com.example.mobileappporject.ui.auth.login

sealed interface LoginEvent {
    data class EmailChanged(val value: String) : LoginEvent
    data class PasswordChanged(val value: String) : LoginEvent
    data class RememberMeChanged(val value: Boolean) : LoginEvent
    data object TogglePasswordVisibility : LoginEvent
    data object Submit : LoginEvent
    data object GoogleSignInClicked : LoginEvent
    data object ClearVerificationPending : LoginEvent
}
