package com.example.mobileappporject.ui.auth.register

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val message: String? = null,
    val isComplete: Boolean = false
)
