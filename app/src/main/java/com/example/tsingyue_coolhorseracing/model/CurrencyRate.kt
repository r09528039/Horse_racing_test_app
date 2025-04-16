package com.example.tsingyue_coolhorseracing.model

import com.google.gson.annotations.SerializedName

data class CurrencyRate(
    val result: String? = null,
    @SerializedName("base_code")
    val baseCode: String? = null,
    val rates: Map<String, Double>? = null
    // Only include fields that the API actually returns
)