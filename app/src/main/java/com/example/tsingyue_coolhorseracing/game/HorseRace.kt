package com.example.tsingyue_coolhorseracing.game

import android.os.Handler
import android.os.Looper
import com.example.tsingyue_coolhorseracing.model.Horse
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.roundToInt

class HorseRace(private val trackLength: Int = 100) {
    private val _horses = MutableList(4) { index -> Horse(id = index + 1) }
    val horses: List<Horse> = _horses

    private val _raceState = MutableStateFlow<RaceState>(RaceState.NotStarted)
    val raceState: StateFlow<RaceState> = _raceState

    private val _positions = MutableStateFlow(List(4) { 0 })
    val positions: StateFlow<List<Int>> = _positions

    private val _winner = MutableStateFlow<Horse?>(null)
    val winner: StateFlow<Horse?> = _winner

    private var raceJob: Job? = null

    fun placeBet(horseId: Int, amount: Double): Boolean {
        if (_raceState.value != RaceState.NotStarted) {
            return false
        }

        return true
    }

    fun startRace() {
        if (_raceState.value != RaceState.NotStarted && _raceState.value != RaceState.Finished) {
            return
        }

        _raceState.value = RaceState.Running
        _winner.value = null

        // Reset positions
        _horses.forEach { it.position = 0 }
        _positions.value = List(4) { 0 }

        // Create a thread for each horse
        val horseThreads = _horses.mapIndexed { index, horse ->
            Thread {
                var finished = false
                while (!finished && _raceState.value == RaceState.Running) {
                    // Move the horse
                    val moveDistance = ThreadLocalRandom.current().nextInt(1, 6)
                    horse.position = (horse.position + moveDistance).coerceAtMost(trackLength)

                    // Update positions in UI thread
                    Handler(Looper.getMainLooper()).post {
                        val currentPositions = _positions.value.toMutableList()
                        currentPositions[index] = horse.position
                        _positions.value = currentPositions

                        // Check if this horse has won
                        if (horse.position >= trackLength && _winner.value == null) {
                            _winner.value = horse
                            _raceState.value = RaceState.Finished
                            finished = true

                            // Update odds based on race result
                            _horses.forEach { h ->
                                h.updateOdds(h.id == horse.id)
                            }
                        }
                    }

                    // Sleep to control race speed
                    Thread.sleep(100)
                }
            }.apply { start() }
        }
    }

    fun getWinnerHorseNumber(): Int {
        return _winner.value?.id ?: 0
    }

    fun calculateWinnings(betAmount: Double, horseNumber: Int): Double {
        val winner = _winner.value ?: return 0.0
        if (winner.id != horseNumber) return 0.0

        val winAmount = betAmount * winner.odds
        return (winAmount * 100).roundToInt() / 100.0 // Round to 2 decimal places
    }

    fun resetRace() {
        raceJob?.cancel()
        _raceState.value = RaceState.NotStarted
        _winner.value = null
    }

    enum class RaceState {
        NotStarted,
        Running,
        Finished
    }
}