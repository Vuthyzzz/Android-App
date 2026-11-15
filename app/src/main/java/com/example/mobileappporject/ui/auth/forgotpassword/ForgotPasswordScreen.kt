package com.example.mobileappporject.ui.auth.forgotpassword

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobileappporject.ui.auth.AuthFeedback
import com.example.mobileappporject.ui.auth.AuthInput
import com.example.mobileappporject.ui.auth.AuthNavigationPrompt
import com.example.mobileappporject.ui.auth.AuthPage
import com.example.mobileappporject.ui.auth.AuthPrimaryButton

@Composable
fun ForgotPasswordScreen(
    state: ForgotPasswordState,
    onEvent: (ForgotPasswordEvent) -> Unit,
    onLogin: () -> Unit
) {
    AuthPage(
        title = "Forgot password?",
        subtitle = "Enter the email linked to your account and we’ll send a verification code."
    ) {
        Text("Reset your password", fontSize = 20.sp)
        Spacer(Modifier.height(18.dp))
        AuthInput(
            value = state.email,
            onValueChange = { onEvent(ForgotPasswordEvent.EmailChanged(it)) },
            label = "Email address",
            placeholder = "you@example.com",
            keyboardType = KeyboardType.Email
        )
        Spacer(Modifier.height(8.dp))
        AuthFeedback(state.message)
        AuthPrimaryButton("Send verification code") {
            onEvent(ForgotPasswordEvent.Submit)
        }
        Spacer(Modifier.height(8.dp))
        AuthNavigationPrompt("Remembered your password?", "Sign in", onLogin)
    }
}
