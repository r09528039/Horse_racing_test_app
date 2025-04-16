package com.example.tsingyue_coolhorseracing.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CurrencyRepository {
    private val apiService = CurrencyApiService.create()
    private var cachedRate: Double? = null
    private var lastFetchTime: Long = 0

    suspend fun getUsdToTwdRate(): Double {
        // Check if we have a cached rate that's less than 1 hour old
        val currentTime = System.currentTimeMillis()
        if (cachedRate != null && currentTime - lastFetchTime < 60 * 60 * 1000) {
            return cachedRate!!
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getLatestRates()
                if (response.isSuccessful && response.body() != null) {
                    val currencyRate = response.body()!!
                    // Only access 'rates' since that's what we defined in our model
                    val rate = currencyRate.rates?.get("TWD") ?: 30.0

                    Log.d("CurrencyRepository", "Fetched rate: $rate")
                    cachedRate = rate
                    lastFetchTime = currentTime
                    rate
                } else {
                    Log.e("CurrencyRepository", "Failed to get rates: ${response.code()} - ${response.errorBody()?.string()}")
                    cachedRate ?: 30.0 // Default to 30 if API fails and no cached value
                }
            } catch (e: Exception) {
                Log.e("CurrencyRepository", "Exception when fetching rates", e)
                cachedRate ?: 30.0 // Default to 30 if API fails and no cached value
            }
        }
    }
}