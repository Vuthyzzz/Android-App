package com.example.mobileappporject.ui.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobileappporject.ui.auth.AuthInk
import com.example.mobileappporject.ui.auth.AuthMutedInk

@Composable
fun DailyProgressCard(
    streakDays: Int = 5,
    xpEarned: Int = 340
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🔥 $streakDays Days Streak", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AuthInk)
            Spacer(modifier = Modifier.height(4.dp))
            Text("⭐ $xpEarned XP Earned", color = AuthMutedInk, fontSize = 13.sp)
        }
    }
}
