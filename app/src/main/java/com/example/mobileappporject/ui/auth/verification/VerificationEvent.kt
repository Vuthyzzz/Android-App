package com.example.mobileappporject.ui.auth.verification

sealed interface VerificationEvent {
    data class CodeChanged(val value: String) : VerificationEvent
    data object Verify : VerificationEvent
    data object Resend : VerificationEvent
    data object ClearCompletion : VerificationEvent
}
