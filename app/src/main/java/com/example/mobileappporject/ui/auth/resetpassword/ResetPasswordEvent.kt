package com.example.mobileappporject.ui.auth.resetpassword

sealed interface ResetPasswordEvent {
    data class Start(val email: String) : ResetPasswordEvent
    data class PasswordChanged(val value: String) : ResetPasswordEvent
    data class ConfirmPasswordChanged(val value: String) : ResetPasswordEvent
    data object TogglePasswordVisibility : ResetPasswordEvent
    data object Submit : ResetPasswordEvent
    data object ClearCompletion : ResetPasswordEvent
}
