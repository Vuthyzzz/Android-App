package com.example.mobileappporject.ui.auth.forgotpassword

sealed interface ForgotPasswordEvent {
    data class EmailChanged(val value: String) : ForgotPasswordEvent
    data object Submit : ForgotPasswordEvent
    data object ClearCompletion : ForgotPasswordEvent
}
