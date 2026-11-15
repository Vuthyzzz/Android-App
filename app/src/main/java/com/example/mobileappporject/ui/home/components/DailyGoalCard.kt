package com.example.mobileappporject.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobileappporject.ui.auth.AuthAccent

@Composable
fun DailyGoalCard(completedMins: Int = 15, totalMins: Int = 20) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AuthAccent),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Daily Goal", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                Text("$completedMins / $totalMins mins", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { completedMins.toFloat() / totalMins },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.3f),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("5 mins left to maintain your 5-day streak!", color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp)
        }
    }
}
