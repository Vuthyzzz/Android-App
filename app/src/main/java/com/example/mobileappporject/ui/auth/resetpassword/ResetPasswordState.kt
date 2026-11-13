package com.example.mobileappporject.ui.auth.resetpassword

data class ResetPasswordState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val message: String? = null,
    val isComplete: Boolean = false
)
