package com.example.mobileappporject.ui.auth.register

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
fun RegisterScreen(
    state: RegisterState,
    onEvent: (RegisterEvent) -> Unit,
    onLogin: () -> Unit
) {
    AuthPage(
        title = "Start your next chapter",
        subtitle = "Create an account and make every day a little smarter."
    ) {
        Text("Create account", fontSize = 20.sp)
        Spacer(Modifier.height(18.dp))
        AuthInput(
            value = state.name,
            onValueChange = { onEvent(RegisterEvent.NameChanged(it)) },
            label = "Full name",
            placeholder = "Alex Morgan"
        )
        Spacer(Modifier.height(14.dp))
        AuthInput(
            value = state.email,
            onValueChange = { onEvent(RegisterEvent.EmailChanged(it)) },
            label = "Email address",
            placeholder = "you@example.com",
            keyboardType = KeyboardType.Email
        )
        Spacer(Modifier.height(14.dp))
        AuthInput(
            value = state.password,
            onValueChange = { onEvent(RegisterEvent.PasswordChanged(it)) },
            label = "Password",
            placeholder = "At least 8 characters",
            keyboardType = KeyboardType.Password,
            password = true,
            passwordVisible = state.passwordVisible,
            onTogglePassword = { onEvent(RegisterEvent.TogglePasswordVisibility) }
        )
        Spacer(Modifier.height(14.dp))
        AuthInput(
            value = state.confirmPassword,
            onValueChange = { onEvent(RegisterEvent.ConfirmPasswordChanged(it)) },
            label = "Confirm password",
            placeholder = "Enter your password again",
            keyboardType = KeyboardType.Password,
            password = true,
            passwordVisible = state.passwordVisible,
            onTogglePassword = { onEvent(RegisterEvent.TogglePasswordVisibility) }
        )
        AuthFeedback(state.message)
        AuthPrimaryButton("Create account") { onEvent(RegisterEvent.Submit) }
        Spacer(Modifier.height(8.dp))
        AuthNavigationPrompt("Already have an account?", "Sign in", onLogin)
    }
}
