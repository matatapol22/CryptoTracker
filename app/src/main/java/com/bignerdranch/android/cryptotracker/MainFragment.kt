package com.bignerdranch.android.cryptotracker

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.util.Locale

class MainFragment : Fragment() {

    private lateinit var viewModel: CryptoViewModel
    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var chart: LineChart
    private lateinit var spinner: Spinner
    private lateinit var cryptoName: TextView
    private lateinit var cryptoInput: EditText
    private lateinit var cryptoIcon: ImageView
    private lateinit var favoriteButton: Button
    private lateinit var searchButton: Button

    private var currentCoinDetails: CoinDetailsResponse? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Привязка View
        chart = view.findViewById(R.id.chart)
        spinner = view.findViewById(R.id.intervalSpinner)
        cryptoName = view.findViewById(R.id.cryptoName)
        cryptoInput = view.findViewById(R.id.cryptoInput)
        cryptoIcon = view.findViewById(R.id.cryptoIcon)
        favoriteButton = view.findViewById(R.id.favoriteButton)
        searchButton = view.findViewById(R.id.searchButton)

        // ViewModel
        viewModel = ViewModelProvider(this)[CryptoViewModel::class.java]
        favoritesViewModel = ViewModelProvider(this)[FavoritesViewModel::class.java]

        setupChart()
        setupSpinner()

        viewModel.coinDetails.observe(viewLifecycleOwner) { details ->
            currentCoinDetails = details
            cryptoName.text = details.name
            Glide.with(requireContext())
                .load(details.image.small)
                .into(cryptoIcon)
            cryptoIcon.visibility = View.VISIBLE
        }

        viewModel.historicalData.observe(viewLifecycleOwner) { entries ->
            updateChart(entries)
        }

        searchButton.setOnClickListener { loadData() }

        favoriteButton.setOnClickListener {
            currentCoinDetails?.let { coin ->
                val favoriteCoin = FavoriteCoin(
                    id = coin.id,
                    name = coin.name,
                    imageUrl = coin.image.small
                )
                favoritesViewModel.addFavorite(favoriteCoin)
                Toast.makeText(requireContext(), "${coin.name} добавлен в избранное", Toast.LENGTH_SHORT).show()
            } ?: run {
                Toast.makeText(requireContext(), "Сначала загрузите криптовалюту", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        val cryptoId = cryptoInput.text.toString().trim().lowercase(Locale.ROOT)

        if (cryptoId.isNotEmpty()) {
            val label = spinner.selectedItem.toString()
            val intervalsMap = mapOf(
                "1д" to "1",
                "7д" to "7",
                "30д" to "30",
                "90д" to "90",
                "1г" to "365"
            )
            val interval = intervalsMap[label] ?: "7"
            cryptoName.text = cryptoId.replaceFirstChar { it.uppercase() }

            viewModel.getHistoricalData(cryptoId, interval)
            viewModel.getCoinDetails(cryptoId)
        } else {
            Toast.makeText(requireContext(), "Введите ID криптовалюты", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSpinner() {
        val intervalsMap = mapOf(
            "1д" to "1",
            "7д" to "7",
            "30д" to "30",
            "90д" to "90",
            "1г" to "365"
        )

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, intervalsMap.keys.toList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                loadData()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupChart() {
        chart.setTouchEnabled(true)
        chart.setPinchZoom(true)
        chart.setBackgroundColor(Color.TRANSPARENT)
        chart.setNoDataText("Нет данных для отображения")

        val marker = CryptoMarkerView(requireContext(), R.layout.marker_view)
        marker.chartView = chart
        chart.marker = marker
    }

    private fun updateChart(entries: List<Entry>) {
        val dataSet = LineDataSet(entries, "Цена USD").apply {
            mode = LineDataSet.Mode.CUBIC_BEZIER
            color = Color.parseColor("#0088FF")
            lineWidth = 2f
            setDrawCircles(true)
            setCircleColor(Color.parseColor("#0088FF"))
            circleRadius = 4f
            setDrawCircleHole(false)
            setDrawValues(false)
            setDrawFilled(true)
            fillColor = Color.parseColor("#D6EBFF")
            fillAlpha = 150
        }

        chart.xAxis.apply {
            valueFormatter = DateAxisFormatter()
            position = XAxis.XAxisPosition.BOTTOM
            textColor = Color.GRAY
            setDrawGridLines(false)
            setAvoidFirstLastClipping(true)
            granularity = 24 * 60 * 60 * 1000f
            labelRotationAngle = -45f
        }

        chart.axisRight.isEnabled = false
        chart.axisLeft.textColor = Color.GRAY
        chart.legend.isEnabled = false
        chart.description.isEnabled = false
        chart.data = LineData(dataSet)
        chart.invalidate()
    }
}