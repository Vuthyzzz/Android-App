package com.example.mobileappporject

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.mobileappporject.ui.auth.AuthFlowScreen
import com.example.mobileappporject.ui.main.MainAppScreen
import com.example.mobileappporject.ui.theme.MobileAppPorjectTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : ComponentActivity() {
    private var isLoggedInState by mutableStateOf(FirebaseAuth.getInstance().currentUser != null)
    private var pendingVerificationEmail by mutableStateOf<String?>(null)
    private lateinit var googleSignInClient: GoogleSignInClient

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val email = account.email ?: "user.google@gmail.com"
            val idToken = account.idToken

            if (idToken.isNullOrBlank()) {
                Toast.makeText(
                    this,
                    "Google Sign-In is not configured for Firebase. Add the web client ID.",
                    Toast.LENGTH_LONG
                ).show()
                return@registerForActivityResult
            }

            val credential = GoogleAuthProvider.getCredential(idToken, null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this) { authTask ->
                    if (authTask.isSuccessful) {
                        pendingVerificationEmail = email
                        isLoggedInState = false
                    } else {
                        Toast.makeText(
                            this,
                            authTask.exception?.message ?: "Google sign-in failed.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        } catch (e: ApiException) {
            Toast.makeText(this, "Google sign-in was cancelled or failed.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {
            MobileAppPorjectTheme(darkTheme = false, dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    fun startGoogleLogin() {
                        val webClientId = getString(R.string.default_web_client_id).trim()
                        if (webClientId.isBlank() || webClientId == "YOUR_WEB_CLIENT_ID") {
                            Toast.makeText(this@MainActivity, "Google Sign-In is not configured. Add your Firebase web client ID.", Toast.LENGTH_LONG).show()
                            return
                        }

                        val googleOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(webClientId)
                            .requestEmail()
                            .build()

                        googleSignInClient = GoogleSignIn.getClient(this@MainActivity, googleOptions)
                        googleSignInLauncher.launch(googleSignInClient.signInIntent)
                    }

                    if (isLoggedInState) {
                        MainAppScreen(onLogout = {
                            FirebaseAuth.getInstance().signOut()
                            isLoggedInState = false
                        })
                    } else {
                        AuthFlowScreen(
                            onLoginSuccess = {
                                isLoggedInState = true
                                pendingVerificationEmail = null
                            },
                            onGoogleSignInRequest = { startGoogleLogin() },
                            initialVerificationEmail = pendingVerificationEmail
                        )
                    }
                }
            }
        }
    }
}
