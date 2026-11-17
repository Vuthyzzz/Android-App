package com.example.mobileappporject.ui.practice

import android.Manifest
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobileappporject.ui.auth.AuthAccent
import com.example.mobileappporject.ui.auth.AuthCanvas
import com.example.mobileappporject.ui.auth.AuthInk
import com.example.mobileappporject.ui.auth.AuthMutedInk
import kotlinx.coroutines.delay
import java.io.File
import kotlin.math.max
import kotlin.time.Duration.Companion.seconds

@Composable
fun SpeakingTestScreen(
    onBack: () -> Unit,
    viewModel: SpeakingTestViewModel = viewModel()
) {
    val context = LocalContext.current
    var topic by remember { mutableStateOf("Daily conversation") }
    var targetSentence by remember {
        mutableStateOf("I would like to book a table for two tonight.")
    }
    var selectedLevel by remember { mutableStateOf("Intermediate") }
    var selectedFocus by remember { mutableStateOf("Pronunciation") }

    var speakingText by remember { mutableStateOf("") }
    var isRecording by remember { mutableStateOf(false) }
    var recordingDuration by remember { mutableStateOf(0L) }
    var audioPath by remember { mutableStateOf<String?>(null) }
    var voiceFeedback by remember { mutableStateOf("No recording yet. Press record to start your speaking practice.") }
    var recorder by remember { mutableStateOf<MediaRecorder?>(null) }
    var player by remember { mutableStateOf<MediaPlayer?>(null) }

    val aiTemplate by viewModel.template.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val feedback by viewModel.feedback.collectAsState()
    val feedbackLoading by viewModel.feedbackLoading.collectAsState()
    val history by viewModel.history.collectAsState()

    fun startRecordingInternal() {
        val outputFile = File(context.cacheDir, "speaking_${System.currentTimeMillis()}.m4a")
        @Suppress("DEPRECATION")
        val newRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFile.absolutePath)
            prepare()
            start()
        }
        recorder = newRecorder
        audioPath = outputFile.absolutePath
        isRecording = true
        recordingDuration = 0
        voiceFeedback = "Recording in progress..."
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            startRecordingInternal()
        } else {
            voiceFeedback = "Microphone permission is required to record your pronunciation practice."
        }
    }

    fun startRecording() {
        if (androidx.core.content.ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            startRecordingInternal()
        } else {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    LaunchedEffect(isRecording) {
        if (isRecording) {
            while (isRecording) {
                delay(1.seconds)
                recordingDuration += 1
            }
        }
    }

    fun stopRecording() {
        try {
            recorder?.apply {
                stop()
                release()
            }
            recorder = null
        } catch (_: Exception) {
            Log.e("SpeakingTestScreen", "Error stopping recording")
        } finally {
            isRecording = false
        }

        val words = targetSentence.split(Regex("\\s+")).size
        val speakingRate = if (recordingDuration > 0) {
            words.toFloat() / max(recordingDuration / 60f, 0.5f)
        } else {
            0f
        }

        val baseScore = when {
            recordingDuration < 3 -> 64
            speakingRate < 90f -> 72
            speakingRate > 180f -> 84
            else -> 88
        }

        val similarity = if (speakingText.isNotBlank()) {
            viewModel.compareSentenceSimilarity(targetSentence, speakingText)
        } else {
            baseScore
        }

        val finalScore = ((baseScore * 0.7) + (similarity * 0.3)).toInt().coerceIn(0, 100)

        viewModel.saveResult(
            sentence = targetSentence,
            durationSeconds = recordingDuration,
            wordsSpoken = words,
            similarityPercent = similarity,
            finalScore = finalScore
        )

        voiceFeedback = buildString {
            append("AI pronunciation review: ")
            append("Score ${finalScore}/100. ")
            append("Similarity ${similarity}% with the target sentence. ")
            append("You spoke for ${recordingDuration}s, pace ${"%.0f".format(speakingRate)} words/min. ")
            append("Focus on clear word endings, natural stress, and smoother rhythm.")
        }

        viewModel.analyzePronunciation(
            sentence = targetSentence,
            level = selectedLevel,
            focus = selectedFocus,
            recordingSeconds = recordingDuration,
            wordsSpoken = words
        )
    }

    fun playRecording() {
        val filePath = audioPath ?: return
        try {
            player?.release()
            val mediaPlayer = MediaPlayer().apply {
                setDataSource(filePath)
                prepare()
                start()
            }
            player = mediaPlayer
            voiceFeedback = "Playing your recorded pronunciation..."
        } catch (e: Exception) {
            Log.e("SpeakingTestScreen", "Could not play recording", e)
            voiceFeedback = "The recording could not be played. Please record again."
        }
    }

    LaunchedEffect(Unit) {
        viewModel.generateTemplate(topic, targetSentence, selectedLevel, selectedFocus)
        viewModel.loadHistory()
    }

    val templateText = if (aiTemplate.isBlank()) {
        """
        AI Pronunciation Template
        Topic: $topic
        Level: $selectedLevel
        Focus: $selectedFocus

        Read out loud: "$targetSentence"

        Instructions:
        1. Speak clearly and naturally at a comfortable pace.
        2. Emphasize difficult sounds and word endings.
        3. Keep steady rhythm and correct intonation.
        4. Repeat the sentence twice for better fluency.
        5. Ask the AI to score pronunciation, rhythm, clarity, and confidence.
        """.trimIndent()
    } else {
        aiTemplate
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuthCanvas)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Text("←", fontSize = 24.sp, color = AuthInk)
            }
            Text(
                text = "Speaking Test",
                color = AuthInk,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Create a pronunciation challenge with an AI-ready speaking template.",
            color = AuthMutedInk,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Topic", color = AuthInk, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = topic,
                    onValueChange = { topic = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text("Sentence to practice", color = AuthInk, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = targetSentence,
                    onValueChange = { targetSentence = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text("Your spoken sentence (optional)", color = AuthInk, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = speakingText,
                    onValueChange = { speakingText = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Level", color = AuthInk, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(listOf("Beginner", "Intermediate", "Advanced")) { level ->
                val isSelected = selectedLevel == level
                OutlinedButton(
                    onClick = { selectedLevel = level },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isSelected) Color(0xFFEEF0FF) else Color.White,
                        contentColor = if (isSelected) AuthAccent else AuthInk
                    )
                ) {
                    Text(level)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Focus area", color = AuthInk, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(listOf("Pronunciation", "Fluency", "Intonation", "Confidence")) { focus ->
                val isSelected = selectedFocus == focus
                OutlinedButton(
                    onClick = { selectedFocus = focus },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isSelected) Color(0xFFEEF0FF) else Color.White,
                        contentColor = if (isSelected) AuthAccent else AuthInk
                    )
                ) {
                    Text(focus)
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        Button(
            onClick = {
                viewModel.generateTemplate(topic, targetSentence, selectedLevel, selectedFocus)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = AuthAccent),
            shape = RoundedCornerShape(14.dp),
            enabled = loading == false
        ) {
            Text(if (loading) "Generating..." else "Generate AI speaking template", fontSize = 15.sp)
        }

        val errorText = error
        if (!errorText.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = errorText,
                color = Color(0xFFB42318),
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F5FF)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "AI Prompt Template",
                    color = AuthInk,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = Color(0xFFE7EAF8))
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = templateText,
                    color = AuthInk,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Pronunciation practice",
                    color = AuthInk,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = if (isRecording) "Recording for ${recordingDuration}s" else "Tap record and speak the target sentence clearly.",
                    color = AuthMutedInk,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { startRecording() },
                        enabled = !isRecording,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = AuthAccent)
                    ) {
                        Text("Record")
                    }

                    Button(
                        onClick = { if (isRecording) stopRecording() else playRecording() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1AA0A0))
                    ) {
                        Text(if (isRecording) "Stop" else "Play")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = voiceFeedback,
                    color = AuthInk,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
                if (feedbackLoading) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Analyzing your pronunciation...", color = AuthMutedInk, fontSize = 12.sp)
                } else if (feedback.isNotBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = feedback,
                        color = AuthInk,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        if (history.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            Text("Recent pronunciation history", color = AuthInk, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            history.forEach { result ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(result.timestamp, color = AuthMutedInk, fontSize = 11.sp)
                        Text(result.sentence, color = AuthInk, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Score: ${result.score}/100  •  Match: ${result.matchPercent}%  •  ${result.durationSeconds}s",
                            color = AuthMutedInk,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Button(
                onClick = { startRecording() },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1AA0A0))
            ) {
                Text("Start speaking practice")
            }
        }
    }
}
