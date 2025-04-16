package com.example.tsingyue_coolhorseracing.network

import android.util.Log
import com.example.tsingyue_coolhorseracing.model.CurrencyRate
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiService {
    @GET("latest")
    suspend fun getLatestRates(
        @Query("base") base: String = "USD"
    ): Response<CurrencyRate>

    companion object {
        // Using a free, reliable API that doesn't require an API key
        private const val BASE_URL = "https://open.er-api.com/v6/"

        fun create(): CurrencyApiService {
            Log.d("CurrencyApiService", "Creating API service with base URL: $BASE_URL")
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CurrencyApiService::class.java)
        }
    }
}