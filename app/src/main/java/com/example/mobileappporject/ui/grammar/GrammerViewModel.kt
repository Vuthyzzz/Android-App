package com.example.mobileappporject.ui.grammar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileappporject.data.remote.dto.GrammarDto
import com.example.mobileappporject.data.repository.GrammarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GrammarViewModel : ViewModel() {

    private val repository = GrammarRepository()

    private val _grammar = MutableStateFlow<List<GrammarDto>>(emptyList())
    val grammar: StateFlow<List<GrammarDto>> = _grammar

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadGrammar() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                _grammar.value = repository.getGrammar()
            } catch (e: Exception) {
                _error.value = e.message ?: "Unable to load Grammar"
            } finally {
                _loading.value = false
            }
        }
    }
}
