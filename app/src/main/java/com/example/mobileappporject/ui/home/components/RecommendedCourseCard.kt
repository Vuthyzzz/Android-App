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
fun RecommendedCourseCard(
    title: String,
    lessons: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(icon, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, color = AuthInk, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(lessons, color = AuthMutedInk, fontSize = 11.sp)
        }
    }
}
