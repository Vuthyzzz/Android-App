package com.example.mobileappporject.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
fun ContinueLearningCard(
    title: String = "Advanced Grammar: Conditionals",
    subtitle: String = "Lesson 4 of 8 • 60% completed",
    icon: String = "📘"
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color(0xFFEEF0FF), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(icon, fontSize = 20.sp)
                }
                Column(modifier = Modifier.padding(start = 14.dp).weight(1f)) {
                    Text(title, color = AuthInk, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                    Text(subtitle, color = AuthMutedInk, fontSize = 12.sp)
                }
            }
        }
    }
}
