package com.bignerdranch.android.cryptotracker

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.util.Locale

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: CryptoViewModel
    private lateinit var chart: LineChart
    private lateinit var spinner: Spinner

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chart = findViewById(R.id.chart)
        spinner = findViewById(R.id.intervalSpinner)
        viewModel = ViewModelProvider(this)[CryptoViewModel::class.java]

        setupSpinner()
        setupChart()

        // Наблюдение за изменением данных
        viewModel.historicalData.observe(this) { entries ->
            updateChart(entries)
        }

        // Загружаем данные по умолчанию
        viewModel.getHistoricalData("bitcoin", "7")
    }

    private fun setupSpinner() {
        val intervalsMap = mapOf(
            "1д" to "1",
            "7д" to "7",
            "30д" to "30",
            "90д" to "90",
            "1г" to "365"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, intervalsMap.keys.toList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val label = parent.getItemAtPosition(position) as String
                val interval = intervalsMap[label] ?: "7"
                viewModel.getHistoricalData("bitcoin", interval)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupChart() {
        chart.setTouchEnabled(true)
        chart.setPinchZoom(true)
        chart.setBackgroundColor(Color.TRANSPARENT)
        chart.setNoDataText("Нет данных для отображения")

        val marker = CryptoMarkerView(this, R.layout.marker_view)
        marker.chartView = chart
        chart.marker = marker
    }

    private fun updateChart(entries: List<Entry>) {
        val dataSet = LineDataSet(entries, "Цена USD").apply {
            mode = LineDataSet.Mode.CUBIC_BEZIER // сглаживание линий
            color = Color.parseColor("#0088FF")  // ярко-синий
            lineWidth = 2f

            setDrawCircles(true)
            setCircleColor(Color.parseColor("#0088FF"))
            circleRadius = 4f
            setDrawCircleHole(false)

            setDrawValues(false) // скрыть цифры над точками
            setDrawFilled(true) // добавить подложку под линией
            fillColor = Color.parseColor("#D6EBFF") // нежно-голубой
            fillAlpha = 150
        }

        chart.xAxis.apply {
            valueFormatter = DateAxisFormatter()
            position = XAxis.XAxisPosition.BOTTOM
            textColor = Color.GRAY
            setDrawGridLines(false)
            setAvoidFirstLastClipping(true)
            granularity = 24 * 60 * 60 * 1000f // шаг 1 день
            labelRotationAngle = -45f
        }

        val lineData = LineData(dataSet)
        chart.data = lineData

        chart.axisRight.isEnabled = false
        chart.xAxis.apply {
            setDrawGridLines(false)
            setDrawAxisLine(false)
            setDrawLabels(false)
        }
        chart.axisLeft.apply {
            setDrawGridLines(false)
            setDrawAxisLine(false)
            textColor = Color.GRAY
        }
        chart.legend.isEnabled = false
        chart.description.isEnabled = false
        chart.setTouchEnabled(true)
        chart.setPinchZoom(true)
        chart.invalidate()
    }
}