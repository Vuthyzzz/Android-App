package com.example.mobileappporject.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class HomeViewModel : ViewModel() {
    var state by mutableStateOf(HomeState())
        private set

    init {
        loadUserHomeData()
    }

    private fun loadUserHomeData() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val displayName = user.displayName?.ifBlank { null }
                ?: user.email?.substringBefore("@")?.replace(".", " ")?.capitalizeWords()
                ?: "Learner"
            state = state.copy(userName = displayName)
        }
    }
}

private fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") { word -> word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } }
