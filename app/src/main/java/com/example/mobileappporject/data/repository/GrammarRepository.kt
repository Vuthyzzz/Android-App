package com.example.mobileappporject.data.repository

import com.example.mobileappporject.data.remote.RetrofitClient
import com.example.mobileappporject.data.remote.api.GrammarApi
import com.example.mobileappporject.data.remote.dto.GrammarDto

class GrammarRepository(
    private val api: GrammarApi = RetrofitClient.grammarApi
) {
    suspend fun getGrammar(): List<GrammarDto> {
        return try {
            api.getGrammar()
        } catch (e: Exception) {
            // Fallback sample data if Spring Boot API is not currently running
            listOf(
                GrammarDto(1, "Present Simple Tense", "Used for habits, facts, and routines.", "ប្រើប្រាស់សម្រាប់ទម្លាប់ និងការពិតទូទៅ។"),
                GrammarDto(2, "Past Continuous Tense", "Used for actions that were ongoing in the past.", "ប្រើប្រាស់សម្រាប់សកម្មភាពដែលកំពុងកើតឡើងក្នុងអតីតកាល។"),
                GrammarDto(3, "Future Perfect Tense", "Used for actions that will be completed before a future time.", "ប្រើប្រាស់សម្រាប់សកម្មភាពដែលនឹងត្រូវបានបញ្ចប់មុនពេលកំណត់នាអនាគត។")
            )
        }
    }
}
