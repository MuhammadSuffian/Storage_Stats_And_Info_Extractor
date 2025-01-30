package com.example.storage_stats_and_info_extractor

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class LlamaRequest(val model: String, val prompt: String)
data class LlamaResponse(val output: String)

interface LlamaApiService {
    @Headers("Content-Type: application/json")
    @POST("/api/generate")
    suspend fun generateResponse(@Body request: LlamaRequest): LlamaResponse
}

object RetrofitClient {
    private const val BASE_URL = "http://192.168.1.19"

    val instance: LlamaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LlamaApiService::class.java)
    }
}
