package com.example.tsingyue_coolhorseracing

import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tsingyue_coolhorseracing.adapter.HistoryAdapter

class HistoryActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val recyclerView = findViewById<RecyclerView>(R.id.rvHistory)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Create adapter with empty list initially
        adapter = HistoryAdapter(emptyList()) { record ->
            // Handle delete click
            viewModel.deleteBettingRecord(record)
        }
        recyclerView.adapter = adapter

        // Observe history changes
        viewModel.bettingHistory.observe(this, Observer { records ->
            adapter.updateData(records)
        })

        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            viewModel.clearAllHistory()
        }
    }
}