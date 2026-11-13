package com.example.mobileappporject.ui.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobileappporject.ui.auth.AuthCanvas
import com.example.mobileappporject.ui.auth.AuthInk
import com.example.mobileappporject.ui.auth.AuthMutedInk

@Composable
fun ProgressScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuthCanvas)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("Your Progress", color = AuthInk, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Track your English learning milestones", color = AuthMutedInk, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🔥 5", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = AuthInk)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Day Streak", color = AuthMutedInk, fontSize = 12.sp)
                }
            }
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("⭐ 340", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = AuthInk)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("XP Earned", color = AuthMutedInk, fontSize = 12.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Weekly Activity", color = AuthInk, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Mon  Tue  Wed  Thu  Fri  Sat  Sun", color = AuthMutedInk, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("🟢   🟢   🟢   🟢   ⚪   ⚪   ⚪", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("4 days active this week. Keep going!", color = AuthMutedInk, fontSize = 12.sp)
            }
        }
    }
}
