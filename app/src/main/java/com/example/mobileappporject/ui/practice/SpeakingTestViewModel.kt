package com.example.mobileappporject.ui.practice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class PronunciationResult(
    val sentence: String,
    val score: Int,
    val durationSeconds: Long,
    val wordsSpoken: Int,
    val matchPercent: Int,
    val timestamp: String
)

class SpeakingTestViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val currentUserId: String?
        get() = FirebaseAuth.getInstance().currentUser?.uid

    private val _template = MutableStateFlow("")
    val template: StateFlow<String> = _template

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _feedback = MutableStateFlow("")
    val feedback: StateFlow<String> = _feedback

    private val _feedbackLoading = MutableStateFlow(false)
    val feedbackLoading: StateFlow<Boolean> = _feedbackLoading

    private val _history = MutableStateFlow<List<PronunciationResult>>(emptyList())
    val history: StateFlow<List<PronunciationResult>> = _history

    fun generateTemplate(topic: String, sentence: String, level: String, focus: String) {
        val trimmedTopic = topic.trim()
        val trimmedSentence = sentence.trim()

        if (trimmedTopic.isBlank() || trimmedSentence.isBlank()) {
            _error.value = "Topic and sentence are required."
            return
        }

        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val prompt = buildPrompt(trimmedTopic, trimmedSentence, level, focus)
                val model = Firebase.ai.generativeModel(modelName = "gemini-2.0-flash")
                val response = model.generateContent(prompt)
                _template.value = response.text ?: prompt
            } catch (e: Exception) {
                _template.value = buildFallbackTemplate(trimmedTopic, trimmedSentence, level, focus)
                _error.value = e.message ?: "AI generation failed. Please try again."
            } finally {
                _loading.value = false
            }
        }
    }

    private fun buildPrompt(topic: String, sentence: String, level: String, focus: String): String {
        return """
            You are an English pronunciation coach.
            Create a speaking test for a learner at $level level.
            Topic: $topic
            Focus area: $focus
            Practice sentence: "$sentence"

            Give me:
            1. A short AI speaking prompt the learner should read aloud.
            2. 3 pronunciation tips for this sentence.
            3. 2 feedback points to check rhythm, stress, and clarity.
            4. The final answer should be concise and beginner-friendly.
        """.trimIndent()
    }

    private fun buildFallbackTemplate(topic: String, sentence: String, level: String, focus: String): String {
        return """
            AI Pronunciation Template
            Topic: $topic
            Level: $level
            Focus: $focus

            Read aloud: "$sentence"

            Instructions:
            1. Speak clearly and naturally at a comfortable pace.
            2. Stress the important words and keep a smooth rhythm.
            3. Repeat the sentence twice and focus on correct pronunciation.
            4. Check clarity, word endings, and intonation.
            5. A teacher or AI should score fluency, accuracy, and confidence.
        """.trimIndent()
    }

    fun analyzePronunciation(
        sentence: String,
        level: String,
        focus: String,
        recordingSeconds: Long,
        wordsSpoken: Int
    ) {
        val trimmedSentence = sentence.trim()
        if (trimmedSentence.isBlank()) {
            _feedback.value = "Please enter a sentence to practice before analyzing pronunciation."
            return
        }

        viewModelScope.launch {
            _feedbackLoading.value = true
            try {
                val prompt = """
                    You are an English pronunciation coach.
                    Analyze the following speaking exercise and give a short but useful improvement report.
                    Level: $level
                    Focus: $focus
                    Sentence: "$trimmedSentence"
                    Recording duration: ${recordingSeconds}s
                    Estimated words spoken: $wordsSpoken

                    Return:
                    1. Score from 0 to 100
                    2. 3 strengths
                    3. 3 improvement tips
                    4. one short final encouragement sentence
                    Keep it concise and encouraging.
                """.trimIndent()

                val model = Firebase.ai.generativeModel(modelName = "gemini-2.0-flash")
                val response = model.generateContent(prompt)
                _feedback.value = response.text ?: buildFallbackFeedback(trimmedSentence, level, focus, recordingSeconds, wordsSpoken)
            } catch (e: Exception) {
                _feedback.value = buildFallbackFeedback(trimmedSentence, level, focus, recordingSeconds, wordsSpoken)
            } finally {
                _feedbackLoading.value = false
            }
        }
    }

    private fun buildFallbackFeedback(
        sentence: String,
        level: String,
        focus: String,
        recordingSeconds: Long,
        wordsSpoken: Int
    ): String {
        val score = when {
            recordingSeconds < 3 -> 64
            wordsSpoken < 6 -> 72
            wordsSpoken in 6..12 -> 86
            else -> 90
        }

        return """
            Pronunciation feedback
            Score: $score/100
            Level: $level
            Focus: $focus

            Strengths:
            - Clear sentence structure and steady speaking effort.
            - Good attempt to maintain rhythm and confidence.
            - Useful practice for ${sentence.take(40)}...

            Improvements:
            - Slow down slightly and emphasize difficult word endings.
            - Keep a smoother rhythm between words.
            - Repeat the sentence twice to improve fluency and clarity.

            Keep going — your pronunciation is improving well.
        """.trimIndent()
    }

    fun saveResult(
        sentence: String,
        durationSeconds: Long,
        wordsSpoken: Int,
        similarityPercent: Int,
        finalScore: Int
    ) {
        val trimmed = sentence.trim()
        if (trimmed.isBlank()) return

        val result = PronunciationResult(
            sentence = trimmed,
            score = finalScore,
            durationSeconds = durationSeconds,
            wordsSpoken = wordsSpoken,
            matchPercent = similarityPercent,
            timestamp = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date())
        )

        val userId = currentUserId ?: return
        val resultMap = hashMapOf(
            "sentence" to result.sentence,
            "score" to result.score,
            "durationSeconds" to result.durationSeconds,
            "wordsSpoken" to result.wordsSpoken,
            "matchPercent" to result.matchPercent,
            "timestamp" to result.timestamp
        )

        db.collection("users")
            .document(userId)
            .collection("speaking_results")
            .add(resultMap)
            .addOnFailureListener {
                _history.value = listOf(result) + _history.value.take(4)
            }

        _history.value = listOf(result) + _history.value.take(4)
    }

    fun loadHistory() {
        val userId = currentUserId ?: return

        db.collection("users")
            .document(userId)
            .collection("speaking_results")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(5)
            .get()
            .addOnSuccessListener { snapshot ->
                val results = snapshot.documents.mapNotNull { document ->
                    val sentence = document.getString("sentence") ?: return@mapNotNull null
                    val score = document.getLong("score")?.toInt() ?: 0
                    val durationSeconds = document.getLong("durationSeconds") ?: 0L
                    val wordsSpoken = document.getLong("wordsSpoken")?.toInt() ?: 0
                    val matchPercent = document.getLong("matchPercent")?.toInt() ?: 0
                    val timestamp = document.getString("timestamp") ?: "now"
                    PronunciationResult(sentence, score, durationSeconds, wordsSpoken, matchPercent, timestamp)
                }
                _history.value = results
            }
    }

    fun resetHistory() {
        _history.value = emptyList()
    }

    fun compareSentenceSimilarity(target: String, spoken: String): Int {
        val cleanTarget = target.lowercase().trim()
        val cleanSpoken = spoken.lowercase().trim()

        if (cleanTarget.isBlank() && cleanSpoken.isBlank()) return 100
        if (cleanTarget.isBlank() || cleanSpoken.isBlank()) return 0

        val maxLength = maxOf(cleanTarget.length, cleanSpoken.length)
        if (maxLength == 0) return 100

        val distance = levenshtein(cleanTarget, cleanSpoken)
        val similarity = ((maxLength - distance).toDouble() / maxLength.toDouble()) * 100.0
        return similarity.coerceIn(0.0, 100.0).toInt()
    }

    private fun levenshtein(a: String, b: String): Int {
        val dp = Array(a.length + 1) { IntArray(b.length + 1) }
        for (i in 0..a.length) dp[i][0] = i
        for (j in 0..b.length) dp[0][j] = j

        for (i in 1..a.length) {
            for (j in 1..b.length) {
                val cost = if (a[i - 1] == b[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,
                    dp[i][j - 1] + 1,
                    dp[i - 1][j - 1] + cost
                )
            }
        }
        return dp[a.length][b.length]
    }
}
