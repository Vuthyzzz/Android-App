package com.example.mobileappporject.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobileappporject.ui.auth.AuthAccent
import com.example.mobileappporject.ui.auth.AuthCanvas
import com.example.mobileappporject.ui.auth.AuthInk
import com.example.mobileappporject.ui.auth.AuthMutedInk

@Composable
fun HomeScreen(userName: String = "Alex") {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuthCanvas)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Welcome Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Welcome back,", color = AuthMutedInk, fontSize = 14.sp)
                Text(userName, color = AuthInk, fontSize = 24.sp, fontWeight = FontWeight.Bold)
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
        Spacer(modifier = Modifier.height(20.dp))

        // Daily Goal Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = AuthAccent),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Daily Goal", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Text("15 / 20 mins", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = { 0.75f },
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
        Spacer(modifier = Modifier.height(24.dp))

        // Continue Learning Section
        Text("Continue Learning", color = AuthInk, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
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
                        Text("📘", fontSize = 20.sp)
                    }
                    Column(modifier = Modifier.padding(start = 14.dp).weight(1f)) {
                        Text("Advanced Grammar: Conditionals", color = AuthInk, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                        Text("Lesson 4 of 8 • 60% completed", color = AuthMutedInk, fontSize = 12.sp)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Recommended Courses Section
        Text("Recommended for You", color = AuthInk, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🗣️", fontSize = 28.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Fluent Speaking", color = AuthInk, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("12 Lessons", color = AuthMutedInk, fontSize = 11.sp)
                }
            }
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("✍️", fontSize = 28.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Business Writing", color = AuthInk, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("10 Lessons", color = AuthMutedInk, fontSize = 11.sp)
                }
            }
        }
    }
}
