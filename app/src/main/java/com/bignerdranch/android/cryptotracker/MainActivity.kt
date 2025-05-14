package com.bignerdranch.android.cryptotracker

import android.graphics.Color
import android.os.Bundle
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: CryptoViewModel
    private lateinit var chart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchView = findViewById<SearchView>(R.id.searchView)
        val cryptoName = findViewById<TextView>(R.id.cryptoName)
        val cryptoPrice = findViewById<TextView>(R.id.cryptoPrice)
        chart = findViewById(R.id.lineChart)

        viewModel = ViewModelProvider(this).get(CryptoViewModel::class.java)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val coinId = query?.lowercase(Locale.getDefault()) ?: return false
                cryptoName.text = coinId
                viewModel.getCurrentPrice(coinId)
                viewModel.getHistoricalData(coinId, "7") // "7" = 7 days
                return true
            }

            override fun onQueryTextChange(newText: String?) = true
        })

        viewModel.cryptoPrice.observe(this) { price ->
            cryptoPrice.text = "Price: $${price}"
        }

        viewModel.historicalData.observe(this) { entries ->
            val priceFloats = entries.map { it.y } // y — значение цены
            setupChart(chart, priceFloats)
        }
    }

    fun setupChart(chart: LineChart, prices: List<Float>) {
        val entries = prices.mapIndexed { index, value -> Entry(index.toFloat(), value) }
        val dataSet = LineDataSet(entries, "Цена")
        dataSet.color = Color.BLUE
        dataSet.valueTextColor = Color.BLACK
        dataSet.setDrawCircles(true)
        dataSet.setDrawValues(false)

        chart.data = LineData(dataSet)

        // Добавим MarkerView
        val markerView = CryptoMarkerView(chart.context, R.layout.marker_view)
        markerView.chartView = chart
        chart.marker = markerView

        chart.invalidate()
    }
}