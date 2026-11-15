package com.example.mobileappporject.ui.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobileappporject.ui.auth.AuthInk
import com.example.mobileappporject.ui.auth.AuthMutedInk

@Composable
fun WelcomeHeader(displayName: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Welcome back,", color = AuthMutedInk, fontSize = 14.sp)
            Text(displayName, color = AuthInk, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
        Surface(
            shape = CircleShape,
            color = Color(0xFFEEF0FF),
            modifier = Modifier.size(48.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text("🔥", fontSize = 22.sp)
            }
        }
    }
}
