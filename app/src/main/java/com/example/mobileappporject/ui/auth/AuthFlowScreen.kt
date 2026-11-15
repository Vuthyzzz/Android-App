package com.example.mobileappporject.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobileappporject.ui.auth.forgotpassword.ForgotPasswordEvent
import com.example.mobileappporject.ui.auth.forgotpassword.ForgotPasswordScreen
import com.example.mobileappporject.ui.auth.forgotpassword.ForgotPasswordViewModel
import com.example.mobileappporject.ui.auth.login.LoginEvent
import com.example.mobileappporject.ui.auth.login.LoginScreen
import com.example.mobileappporject.ui.auth.login.LoginViewModel
import com.example.mobileappporject.ui.auth.register.RegisterEvent
import com.example.mobileappporject.ui.auth.register.RegisterScreen
import com.example.mobileappporject.ui.auth.register.RegisterViewModel
import com.example.mobileappporject.ui.auth.resetpassword.ResetPasswordEvent
import com.example.mobileappporject.ui.auth.resetpassword.ResetPasswordScreen
import com.example.mobileappporject.ui.auth.resetpassword.ResetPasswordViewModel
import com.example.mobileappporject.ui.auth.verification.VerificationEvent
import com.example.mobileappporject.ui.auth.verification.VerificationPurpose
import com.example.mobileappporject.ui.auth.verification.VerificationScreen
import com.example.mobileappporject.ui.auth.verification.VerificationViewModel

private enum class AuthRoute {
    LOGIN,
    REGISTER,
    FORGOT_PASSWORD,
    VERIFICATION,
    RESET_PASSWORD
}

@Composable
fun AuthFlowScreen(
    onLoginSuccess: () -> Unit,
    onGoogleSignInRequest: (() -> Unit)? = null
) {
    var route by remember { mutableStateOf(AuthRoute.LOGIN) }
    val loginViewModel: LoginViewModel = viewModel()
    val registerViewModel: RegisterViewModel = viewModel()
    val forgotPasswordViewModel: ForgotPasswordViewModel = viewModel()
    val verificationViewModel: VerificationViewModel = viewModel()
    val resetPasswordViewModel: ResetPasswordViewModel = viewModel()

    fun startVerification(email: String, purpose: VerificationPurpose) {
        verificationViewModel.start(email, purpose)
        route = AuthRoute.VERIFICATION
    }

    LaunchedEffect(loginViewModel.state.requiresVerification) {
        if (loginViewModel.state.requiresVerification) {
            startVerification(loginViewModel.state.email, VerificationPurpose.ACCOUNT)
        }
    }

    LaunchedEffect(loginViewModel.state.isLoggedIn) {
        if (loginViewModel.state.isLoggedIn) {
            onLoginSuccess()
            loginViewModel.onEvent(LoginEvent.ClearVerificationPending)
        }
    }

    LaunchedEffect(registerViewModel.state.isComplete) {
        if (registerViewModel.state.isComplete) {
            onLoginSuccess()
            registerViewModel.onEvent(RegisterEvent.ClearCompletion)
        }
    }

    LaunchedEffect(forgotPasswordViewModel.state.isComplete) {
        if (forgotPasswordViewModel.state.isComplete) {
            startVerification(forgotPasswordViewModel.state.email, VerificationPurpose.PASSWORD_RESET)
            forgotPasswordViewModel.onEvent(ForgotPasswordEvent.ClearCompletion)
        }
    }

    LaunchedEffect(verificationViewModel.state.isVerified) {
        if (verificationViewModel.state.isVerified) {
            if (verificationViewModel.state.purpose == VerificationPurpose.PASSWORD_RESET) {
                resetPasswordViewModel.onEvent(
                    ResetPasswordEvent.Start(verificationViewModel.state.email)
                )
                route = AuthRoute.RESET_PASSWORD
            } else {
                loginViewModel.completeLoginAfterVerification()
                onLoginSuccess()
                route = AuthRoute.LOGIN
            }
            verificationViewModel.onEvent(VerificationEvent.ClearCompletion)
        }
    }

    LaunchedEffect(resetPasswordViewModel.state.isComplete) {
        if (resetPasswordViewModel.state.isComplete) {
            route = AuthRoute.LOGIN
            resetPasswordViewModel.onEvent(ResetPasswordEvent.ClearCompletion)
        }
    }

    when (route) {
        AuthRoute.LOGIN -> LoginScreen(
            state = loginViewModel.state,
            onEvent = loginViewModel::onEvent,
            onRegister = { route = AuthRoute.REGISTER },
            onForgotPassword = { route = AuthRoute.FORGOT_PASSWORD },
            onGoogleSignIn = {
                // On Google Sign In, we directly start the verification flow
                startVerification("user@example.com", VerificationPurpose.ACCOUNT)
            }
        )
        AuthRoute.REGISTER -> RegisterScreen(
            state = registerViewModel.state,
            onEvent = registerViewModel::onEvent,
            onLogin = { route = AuthRoute.LOGIN }
        )
        AuthRoute.FORGOT_PASSWORD -> ForgotPasswordScreen(
            state = forgotPasswordViewModel.state,
            onEvent = forgotPasswordViewModel::onEvent,
            onLogin = { route = AuthRoute.LOGIN }
        )
        AuthRoute.VERIFICATION -> VerificationScreen(
            state = verificationViewModel.state,
            onEvent = verificationViewModel::onEvent,
            onBack = {
                route = if (verificationViewModel.state.purpose == VerificationPurpose.ACCOUNT) {
                    AuthRoute.REGISTER
                } else {
                    AuthRoute.FORGOT_PASSWORD
                }
            }
        )
        AuthRoute.RESET_PASSWORD -> ResetPasswordScreen(
            state = resetPasswordViewModel.state,
            onEvent = resetPasswordViewModel::onEvent
        )
    }
}