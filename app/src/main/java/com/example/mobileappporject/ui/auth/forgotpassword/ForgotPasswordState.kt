package com.example.mobileappporject.ui.auth.forgotpassword

data class ForgotPasswordState(
    val email: String = "",
    val message: String? = null,
    val isComplete: Boolean = false
)
