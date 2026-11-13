package com.example.mobileappporject.ui.auth.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = true,
    val passwordVisible: Boolean = false,
    val message: String? = null,
    val isLoggedIn: Boolean = false
)
