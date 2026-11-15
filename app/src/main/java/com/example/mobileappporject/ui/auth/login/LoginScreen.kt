package com.example.mobileappporject.ui.auth.login

import android.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobileappporject.ui.auth.AuthAccent
import com.example.mobileappporject.ui.auth.AuthDivider
import com.example.mobileappporject.ui.auth.AuthFeedback
import com.example.mobileappporject.ui.auth.AuthGoogleButton
import com.example.mobileappporject.ui.auth.AuthInput
import com.example.mobileappporject.ui.auth.AuthMutedInk
import com.example.mobileappporject.ui.auth.AuthNavigationPrompt
import com.example.mobileappporject.ui.auth.AuthPage
import com.example.mobileappporject.ui.auth.AuthPrimaryButton

@Composable
fun LoginScreen(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
    onRegister: () -> Unit,
    onForgotPassword: () -> Unit,
    onGoogleSignIn: () -> Unit
) {
    AuthPage(
        title = "Welcome back",
        subtitle = "Your daily English practice is just a sign in away."
    ) {
        Text("Sign in", fontSize = 20.sp)
        Spacer(Modifier.height(18.dp))

        AuthInput(
            value = state.email,
            onValueChange = { onEvent(LoginEvent.EmailChanged(it)) },
            label = "Email address",
            placeholder = "you@example.com",
            keyboardType = KeyboardType.Email
        )
        Spacer(Modifier.height(14.dp))
        AuthInput(
            value = state.password,
            onValueChange = { onEvent(LoginEvent.PasswordChanged(it)) },
            label = "Password",
            placeholder = "At least 8 characters",
            keyboardType = KeyboardType.Password,
            password = true,
            passwordVisible = state.passwordVisible,
            onTogglePassword = { onEvent(LoginEvent.TogglePasswordVisibility) }
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = state.rememberMe,
                onCheckedChange = { onEvent(LoginEvent.RememberMeChanged(it)) }
            )
            Text("Remember me", color = AuthMutedInk, fontSize = 13.sp)
            Spacer(Modifier.weight(1f))
            TextButton(onClick = onForgotPassword) {
                Text("Forgot password?", color = AuthAccent, fontSize = 13.sp)
            }
        }
        AuthGoogleButton(onClick = onGoogleSignIn)

        AuthDivider("OR SIGN IN WITH EMAIL")

        AuthFeedback(state.message)
        AuthPrimaryButton("Sign in") { onEvent(LoginEvent.Submit) }
        Spacer(Modifier.height(8.dp))
        AuthNavigationPrompt("New to Wordwise?", "Create account", onRegister)
    }
}
