package com.example.tsingyue_coolhorseracing

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {

    private val progressBars by lazy {
        listOf(
            findViewById<ProgressBar>(R.id.progressHorse1),
            findViewById<ProgressBar>(R.id.progressHorse2),
            findViewById<ProgressBar>(R.id.progressHorse3),
            findViewById<ProgressBar>(R.id.progressHorse4)
        )
    }

    private val oddsViews by lazy {
        listOf(
            findViewById<TextView>(R.id.tvOddsHorse1),
            findViewById<TextView>(R.id.tvOddsHorse2),
            findViewById<TextView>(R.id.tvOddsHorse3),
            findViewById<TextView>(R.id.tvOddsHorse4)
        )
    }

    private lateinit var balanceTextView: TextView
    private lateinit var exchangeRateTextView: TextView
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        balanceTextView = findViewById(R.id.tvBalance)
        exchangeRateTextView = findViewById(R.id.tvExchangeRate)

        findViewById<Button>(R.id.btnStartRace).setOnClickListener {
            viewModel.startRace()
        }

        findViewById<Button>(R.id.btnHistory).setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        val betButtons = listOf(
            findViewById<Button>(R.id.btnBetHorse1),
            findViewById<Button>(R.id.btnBetHorse2),
            findViewById<Button>(R.id.btnBetHorse3),
            findViewById<Button>(R.id.btnBetHorse4)
        )

        betButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                showBetDialog(index)
            }
        }

        // Set up observers for LiveData
        setupObservers()
    }

    private fun setupObservers() {
        // Observe balance changes
        viewModel.balance.observe(this, Observer { balance ->
            balanceTextView.text = "$balance TWD"
        })

        // Observe exchange rate changes
        viewModel.exchangeRate.observe(this, Observer { rate ->
            exchangeRateTextView.text = "Exchange rate: 1 USD = $rate TWD"
        })

        // Observe horse positions
        viewModel.horsePositions.observe(this, Observer { positions ->
            positions.forEachIndexed { index, position ->
                progressBars[index].progress = position
            }
        })

        // Observe horse odds
        viewModel.horses.observe(this, Observer { horses ->
            horses.forEachIndexed { index, horse ->
            oddsViews[index].text = "${"%.2f".format(horse.odds)}x"
            }
        })

        // Observe race status
        viewModel.isRaceRunning.observe(this, Observer { isRunning ->
            findViewById<Button>(R.id.btnStartRace).isEnabled = !isRunning
            oddsViews.forEach { it.isEnabled = !isRunning }
        })

        // Observe winning horse
        viewModel.winningHorse.observe(this, Observer { winnerIndex ->
            winnerIndex?.let {
                // Show winning dialog or some indication
                android.app.AlertDialog.Builder(this)
                    .setTitle("Race Result")
                    .setMessage("Horse ${it + 1} is the winner!")
                    .setPositiveButton("OK", null)
                    .show()
            }
        })
    }

    private fun showBetDialog(horseIndex: Int) {
        val dialog = BetDialog(
            context = this,
            horseIndex = horseIndex,
            odds = viewModel.horses.value?.get(horseIndex)?.odds?.toFloat() ?: 2.0f,
            balance = viewModel.balance.value ?: 10000,
            usdToTwd = viewModel.exchangeRate.value ?: 30.0f
        ) { usd, twd ->
            viewModel.placeBet(horseIndex, usd, twd)
        }
        dialog.show()
    }
}