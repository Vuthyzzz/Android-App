package com.example.mobileappporject.ui.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobileappporject.ui.auth.AuthCanvas
import com.example.mobileappporject.ui.auth.AuthInk
import com.example.mobileappporject.ui.auth.AuthMutedInk

@Composable
fun PracticeScreen() {
    val selectedPractice = remember { mutableStateOf<String?>(null) }

    if (selectedPractice.value == "Speaking Test") {
        SpeakingTestScreen(onBack = { selectedPractice.value = null })
        return
    }

    if (selectedPractice.value == "Conversation and Talking with AI") {
        ConversationWithAiScreen(onBack = { selectedPractice.value = null })
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuthCanvas)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("Daily Practice", color = AuthInk, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Sharpen your skills with bite-sized exercises", color = AuthMutedInk, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(20.dp))

        val practices = listOf(
            Triple("Vocabulary Quiz", "Test your memory with 15 quick words", "🃏"),
            Triple("Grammar Check", "Fix sentence errors and rules", "✍️"),
            Triple("Listening Drill", "Listen and type what you hear", "🎧"),
            Triple("Speaking Test", "Pronunciation practice with AI", "🎙️"),
            Triple("Conversation and Talking with AI", "Practice real dialogue and speaking flow with AI", "🤖")
        )

        practices.forEach { (title, subtitle, icon) ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable {
                        if (title == "Speaking Test" || title == "Conversation and Talking with AI") {
                            selectedPractice.value = title
                        }
                    },
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