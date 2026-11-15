package com.example.mobileappporject.data.remote.api

import com.example.mobileappporject.data.remote.dto.GrammarDto
import retrofit2.http.GET

interface GrammarApi {
    @GET("api/grammar")
    suspend fun getGrammar(): List<GrammarDto>
}