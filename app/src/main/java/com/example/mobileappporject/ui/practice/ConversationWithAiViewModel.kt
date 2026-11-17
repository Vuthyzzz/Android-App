package com.example.mobileappporject.ui.practice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ConversationMessage(
    val role: String,
    val text: String
)

class ConversationWithAiViewModel : ViewModel() {

    private val _messages = MutableStateFlow<List<ConversationMessage>>(emptyList())
    val messages: StateFlow<List<ConversationMessage>> = _messages

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun generateStartPrompt(topic: String, goal: String): String {
        val safeTopic = topic.ifBlank { "daily conversation" }
        val safeGoal = goal.ifBlank { "practice speaking naturally" }

        return """
            You are an English conversation partner.
            Topic: $safeTopic
            Goal: $safeGoal

            Start the conversation naturally and friendly.
            Ask one short question about the topic.
            Keep the tone warm and encouraging.
            After the user's answer, continue with a follow-up question.
            Keep responses short and easy to speak aloud.
        """.trimIndent()
    }

    fun startConversation(topic: String, goal: String) {
        val prompt = generateStartPrompt(topic, goal)
        val intro = """
            Hi! Let’s practice English about $topic.
            ${goal.takeIf { it.isNotBlank() } ?: "Let’s keep it natural and friendly."}
            Please answer in a short natural sentence, then I’ll ask a follow-up question.
        """.trimIndent()

        _messages.value = listOf(
            ConversationMessage("ai", intro)
        )
        _error.value = null
    }

    fun sendMessage(topic: String, goal: String, userText: String) {
        val trimmed = userText.trim()
        if (trimmed.isBlank()) {
            _error.value = "Please type a message before sending."
            return
        }

        val currentMessages = _messages.value.toMutableList()
        currentMessages += ConversationMessage("user", trimmed)
        _messages.value = currentMessages
        _error.value = null

        viewModelScope.launch {
            _loading.value = true
            try {
                val conversationHistory = currentMessages.joinToString(separator = "\n") { msg ->
                    "${msg.role.uppercase()}: ${msg.text}"
                }

                val prompt = """
                    You are an English conversation coach.
                    Topic: ${topic.ifBlank { "daily conversation" }}
                    Goal: ${goal.ifBlank { "practice speaking naturally" }}

                    Continue this conversation naturally and help the learner speak confidently.
                    Keep the response short, friendly, and easy to say aloud.
                    Ask one question or give one follow-up sentence at a time.

                    Conversation so far:
                    $conversationHistory
                """.trimIndent()

                val model = Firebase.ai.generativeModel(modelName = "gemini-2.0-flash")
                val response = model.generateContent(prompt)
                val aiReply = response.text ?: "That sounds good! Can you tell me more?"
                _messages.value = currentMessages + ConversationMessage("ai", aiReply)
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to get AI reply. Please try again."
                _messages.value = currentMessages + ConversationMessage(
                    "ai",
                    "That sounds good! Please answer in a short sentence, and I’ll keep the conversation going."
                )
            } finally {
                _loading.value = false
            }
        }
    }
}

