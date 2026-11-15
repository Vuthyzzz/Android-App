package com.example.mobileappporject.navigation

import androidx.compose.runtime.Composable
import com.example.mobileappporject.ui.main.MainAppScreen

@Composable
fun AppNavigation(onLogout: () -> Unit) {
    MainAppScreen(onLogout = onLogout)
}
