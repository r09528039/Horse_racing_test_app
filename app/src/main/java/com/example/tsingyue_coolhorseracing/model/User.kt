package com.example.tsingyue_coolhorseracing.model

data class User(
    var balance: Double = 10000.0, // Initial balance in TWD
    val betHistory: MutableList<BettingRecord> = mutableListOf()
)