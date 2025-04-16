package com.example.tsingyue_coolhorseracing.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tsingyue_coolhorseracing.R
import com.example.tsingyue_coolhorseracing.model.BettingRecord

class HistoryAdapter(
    private var data: List<BettingRecord>,
    private val onDeleteClick: (BettingRecord) -> Unit
) : RecyclerView.Adapter<HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_betting_record, parent, false)
        return HistoryViewHolder(view, onDeleteClick)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    fun updateData(newData: List<BettingRecord>) {
        data = newData
        notifyDataSetChanged()
    }
}

class HistoryViewHolder(
    view: View,
    private val onDeleteClick: (BettingRecord) -> Unit
) : RecyclerView.ViewHolder(view) {
    private val tvBetAmount: TextView = view.findViewById(R.id.tvBetAmount)
    private val tvBetHorse: TextView = view.findViewById(R.id.tvBetHorse)
    private val tvWinnerHorse: TextView = view.findViewById(R.id.tvWinnerHorse)
    private val tvProfit: TextView = view.findViewById(R.id.tvProfit)
    private val tvBalance: TextView = view.findViewById(R.id.tvBalance)
    private val btnDelete: Button = view.findViewById(R.id.btnDelete)

    private var currentRecord: BettingRecord? = null

    fun bind(record: BettingRecord) {
        currentRecord = record

        tvBetAmount.text = "${record.betAmount.toInt()} USD (${record.betAmountInTWD.toInt()} TWD)"
        tvBetHorse.text = "horse ${record.horseNumber}"
        tvWinnerHorse.text = "horse ${record.winnerHorseNumber}"

        val profit = if (record.winAmount > 0) record.winAmount.toInt() else 0
        tvProfit.text = "$profit TWD"
        tvBalance.text = "${record.balanceAfterBet.toInt()} TWD"

        btnDelete.setOnClickListener {
            currentRecord?.let { onDeleteClick(it) }
        }
    }
}