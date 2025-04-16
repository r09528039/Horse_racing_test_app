package com.example.tsingyue_coolhorseracing

data class BetHistory(
    val usd: Int,
    val twd: Int,
    val betHorse: Int,
    var winHorse: Int? = null,
    var profit: Int = 0,
    var balance: Int = 0
)

object HistoryManager {
    private val history = mutableListOf<BetHistory>()
    private var balance = 10000

    fun addHistory(usd: Int, twd: Int, betHorse: Int, odds: Float) {
        history.add(BetHistory(usd, twd, betHorse))
        balance -= twd
    }

    fun updateLastResult(winningHorse: Int) {
        history.lastOrNull()?.let {
            it.winHorse = winningHorse
            if (it.betHorse == winningHorse) {
                it.profit = (it.usd * it.twd / it.usd)
                balance += it.profit + it.twd
            } else {
                it.profit = 0
            }
            it.balance = balance
        }
    }

    fun getHistory(): List<BetHistory> = history

    fun clearHistory() {
        history.clear()
    }
}