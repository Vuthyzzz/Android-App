package com.example.mobileappporject.ui.course

import androidx.compose.foundation.background
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
fun CourseScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuthCanvas)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("Explore Courses", color = AuthInk, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Master English with expert-curated modules", color = AuthMutedInk, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(20.dp))

        val courses = listOf(
            Triple("Grammar Masterclass", "20 Lessons • Beginner to Advanced", "📘"),
            Triple("Daily Conversation", "15 Lessons • Intermediate", "🗣️"),
            Triple("Business English", "10 Lessons • Professional", "💼"),
            Triple("Vocabulary Builder", "30 Lessons • All Levels", "🧠")
        )

        courses.forEach { (title, subtitle, icon) ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Text(icon, fontSize = 32.sp, modifier = Modifier.padding(end = 16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(title, color = AuthInk, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(subtitle, color = AuthMutedInk, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
