package com.example.tsingyue_coolhorseracing

import android.app.AlertDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class BetDialog(
    context: Context,
    private val horseIndex: Int,
    private val odds: Float,
    private val balance: Int,
    private val usdToTwd: Float,
    private val onBetPlaced: (usd: Int, twd: Int) -> Unit
) {
    private val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_bet, null)
    private val etBetAmount = dialogView.findViewById<EditText>(R.id.etBetAmount)
    private val tvBetOdds = dialogView.findViewById<TextView>(R.id.tvBetOdds)
    private val tvBetAmountTWD = dialogView.findViewById<TextView>(R.id.tvBetAmountTWD)
    private val tvMaxBet = dialogView.findViewById<TextView>(R.id.tvMaxBet)

    private val alertDialog: AlertDialog = AlertDialog.Builder(context)
        .setView(dialogView)
        .setCancelable(true)
        .create()

    init {
        tvBetOdds.text = "Current Odd: ${String.format("%.1f", odds)}x"

        // Calculate maximum bet - either 10 USD or maximum affordable based on balance
        val maxUsdBet = minOf(10, (balance / usdToTwd).toInt())
        tvMaxBet.text = "Maximum bet: $maxUsdBet USD"

        // Add text change listener for real-time updates
        etBetAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateTWD()
            }
        })

        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            alertDialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnConfirm).setOnClickListener {
            val usd = etBetAmount.text.toString().toIntOrNull() ?: 0

            // Validate the bet amount
            when {
                usd <= 0 -> {
                    Toast.makeText(context, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                }
                usd > 10 -> {
                    Toast.makeText(context, "Maximum bet is 10 USD", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val twd = (usd * usdToTwd).toInt()
                    if (twd <= balance) {
                        onBetPlaced(usd, twd)
                        alertDialog.dismiss()
                    } else {
                        Toast.makeText(context, "Not enough balance", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun updateTWD() {
        val usd = etBetAmount.text.toString().toFloatOrNull() ?: 0f
        val twd = usd * usdToTwd
        tvBetAmountTWD.text = "Equivalent to: ${twd.toInt()} TWD"
    }

    fun show() = alertDialog.show()
}