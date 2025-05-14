package com.bignerdranch.android.cryptotracker

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: CryptoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bitcoinPrice = findViewById<TextView>(R.id.bitcoinPrice)
        val ethereumPrice = findViewById<TextView>(R.id.ethereumPrice)
        val chart = findViewById<LineChart>(R.id.chart)

        viewModel = ViewModelProvider(this)[CryptoViewModel::class.java]

        viewModel.prices.observe(this) { prices ->
            prices["bitcoin"]?.get("usd")?.let {
                bitcoinPrice.text = "Bitcoin: $${it}"
            }
            prices["ethereum"]?.get("usd")?.let {
                ethereumPrice.text = "Ethereum: $${it}"
            }

            // Демонстрационный график
            val entries = listOf(
                Entry(0f, 30000f),
                Entry(1f, 31000f),
                Entry(2f, 30500f),
                Entry(3f, 32000f)
            )
            val dataSet = LineDataSet(entries, "Bitcoin Price")
            chart.data = LineData(dataSet)
            chart.invalidate()
        }
    }
}