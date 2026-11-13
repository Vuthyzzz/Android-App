package com.example.mobileappporject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.mobileappporject.ui.auth.AuthFlowScreen
import com.example.mobileappporject.ui.main.MainAppScreen
import com.example.mobileappporject.ui.theme.MobileAppPorjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileAppPorjectTheme(dynamicColor = false) {
                var isLoggedIn by remember { mutableStateOf(false) }
                if (isLoggedIn) {
                    MainAppScreen(onLogout = { isLoggedIn = false })
                } else {
                    AuthFlowScreen(onLoginSuccess = { isLoggedIn = true })
                }
            }
        }
    }
}
