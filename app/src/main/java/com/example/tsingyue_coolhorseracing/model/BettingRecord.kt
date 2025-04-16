package com.example.tsingyue_coolhorseracing.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "betting_records")
data class BettingRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val betAmount: Double,         // in USD
    val betAmountInTWD: Double,    // in TWD
    val horseNumber: Int,          // 1-4
    val winnerHorseNumber: Int,    // 1-4
    val winAmount: Double,         // in TWD
    val balanceAfterBet: Double,   // in TWD
    val timestamp: Date = Date()
)