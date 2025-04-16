package com.example.tsingyue_coolhorseracing.model

data class Horse(
    val id: Int,
    var odds: Double = 2.0,
    var position: Int = 0,
    var wins: Int = 0,
    var losses: Int = 0
) {
    fun updateOdds(didWin: Boolean) {
        if (didWin) {
            wins++
            odds = (odds - 0.1).coerceAtLeast(1.2)
        } else {
            losses++
            odds = (odds + 0.1).coerceAtMost(5.0)
        }
    }
}