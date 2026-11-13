package com.example.mobileappporject.ui.auth.verification

enum class VerificationPurpose {
    ACCOUNT,
    PASSWORD_RESET
}

data class VerificationState(
    val email: String = "",
    val purpose: VerificationPurpose = VerificationPurpose.ACCOUNT,
    val code: String = "",
    val message: String? = null,
    val isVerified: Boolean = false
)
