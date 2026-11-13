package com.example.mobileappporject.ui.auth.resetpassword

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
import com.example.mobileappporject.ui.auth.AuthPage
import com.example.mobileappporject.ui.auth.AuthPrimaryButton

@Composable
fun ResetPasswordScreen(
    state: ResetPasswordState,
    onEvent: (ResetPasswordEvent) -> Unit
) {
    AuthPage(
        title = "Create a new password",
        subtitle = "Choose a strong password for ${state.email}."
    ) {
        Text("New password", fontSize = 20.sp)
        Spacer(Modifier.height(18.dp))
        AuthInput(
            value = state.password,
            onValueChange = { onEvent(ResetPasswordEvent.PasswordChanged(it)) },
            label = "Password",
            placeholder = "At least 8 characters",
            keyboardType = KeyboardType.Password,
            password = true,
            passwordVisible = state.passwordVisible,
            onTogglePassword = { onEvent(ResetPasswordEvent.TogglePasswordVisibility) }
        )
        Spacer(Modifier.height(14.dp))
        AuthInput(
            value = state.confirmPassword,
            onValueChange = { onEvent(ResetPasswordEvent.ConfirmPasswordChanged(it)) },
            label = "Confirm password",
            placeholder = "Enter your password again",
            keyboardType = KeyboardType.Password,
            password = true,
            passwordVisible = state.passwordVisible,
            onTogglePassword = { onEvent(ResetPasswordEvent.TogglePasswordVisibility) }
        )
        AuthFeedback(state.message)
        AuthPrimaryButton("Update password") { onEvent(ResetPasswordEvent.Submit) }
    }
}
