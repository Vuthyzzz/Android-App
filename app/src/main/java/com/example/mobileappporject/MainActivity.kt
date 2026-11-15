package com.example.mobileappporject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.mobileappporject.ui.auth.AuthFlowScreen
import com.example.mobileappporject.ui.main.MainAppScreen
import com.example.mobileappporject.ui.theme.MobileAppPorjectTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileAppPorjectTheme(darkTheme = false, dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isLoggedIn by remember {
                        mutableStateOf(FirebaseAuth.getInstance().currentUser != null)
                    }

                    if (isLoggedIn) {
                        MainAppScreen(onLogout = {
                            FirebaseAuth.getInstance().signOut()
                            isLoggedIn = false
                        })
                    } else {
                        AuthFlowScreen(onLoginSuccess = { isLoggedIn = true })
                    }
                }
            }
        }
    }
}
