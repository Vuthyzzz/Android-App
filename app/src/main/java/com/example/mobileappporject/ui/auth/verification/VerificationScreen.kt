package com.example.mobileappporject.ui.auth.verification

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobileappporject.ui.auth.AuthAccent
import com.example.mobileappporject.ui.auth.AuthFeedback
import com.example.mobileappporject.ui.auth.AuthInput
import com.example.mobileappporject.ui.auth.AuthNavigationPrompt
import com.example.mobileappporject.ui.auth.AuthPage
import com.example.mobileappporject.ui.auth.AuthPrimaryButton

@Composable
fun VerificationScreen(
    state: VerificationState,
    onEvent: (VerificationEvent) -> Unit,
    onBack: () -> Unit
) {
    val title = if (state.purpose == VerificationPurpose.ACCOUNT) {
        "Verify your email"
    } else {
        "Check your email"
    }

    AuthPage(
        title = title,
        subtitle = "Enter the 6-digit code sent to ${state.email}."
    ) {
        Text("Verification code", fontSize = 20.sp)
        Spacer(Modifier.height(18.dp))
        AuthInput(
            value = state.code,
            onValueChange = { onEvent(VerificationEvent.CodeChanged(it)) },
            label = "6-digit code",
            placeholder = "123456",
            keyboardType = KeyboardType.Number
        )

        Spacer(Modifier.height(14.dp))
        AuthFeedback(state.message)
        AuthPrimaryButton("Verify code") { onEvent(VerificationEvent.Verify) }
        TextButton(onClick = { onEvent(VerificationEvent.Resend) }) {
            Text("Resend code", color = AuthAccent)
        }
        AuthNavigationPrompt("Need to change your email?", "Go back", onBack)
    }
}
