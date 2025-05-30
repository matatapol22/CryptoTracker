package com.bignerdranch.android.cryptotracker

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var cryptoPrice: TextView
    private var currentCryptoId: String? = null
    private lateinit var cryptoAmountEditText: EditText
    private lateinit var dollarValueTextView: TextView
    private var currentPriceUsd: Double = 0.0

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
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        cryptoPrice = view.findViewById(R.id.cryptoPrice)
        cryptoAmountEditText = view.findViewById(R.id.cryptoAmountEditText)
        dollarValueTextView = view.findViewById(R.id.dollarValueTextView)

        // ViewModel
        viewModel = ViewModelProvider(this)[CryptoViewModel::class.java]
        favoritesViewModel = ViewModelProvider(this)[FavoritesViewModel::class.java]

        setupChart()
        setupSpinner()

        sharedViewModel.selectedCoin.observe(viewLifecycleOwner) { coin ->
            cryptoName.text = coin.name
            currentCryptoId = coin.id
            // Запускаем загрузку данных о выбранной монете
            viewModel.getCoinDetails(coin.id)
            viewModel.getHistoricalData(coin.id, spinner.selectedItem.toString())
        }

        viewModel.coinDetails.observe(viewLifecycleOwner) { details ->
            currentCoinDetails = details
            cryptoName.text = details.name
            Glide.with(requireContext())
                .load(details.image.small)
                .into(cryptoIcon)
            cryptoIcon.visibility = View.VISIBLE

            val price = details.marketData?.currentPrice?.get("usd")
            view.findViewById<TextView>(R.id.cryptoPrice).text = if (price != null) {
                "Цена: $${String.format("%.2f", price)}"
            } else {
                "Цена не найдена"
            }

            currentPriceUsd = details.marketData?.currentPrice?.get("usd") ?: 0.0
            updateDollarValue()
        }



        viewModel.historicalData.observe(viewLifecycleOwner) { entries ->
            updateChart(entries)
        }

        cryptoAmountEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateDollarValue()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

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

    private fun updateDollarValue() {
        val inputAmount = cryptoAmountEditText.text.toString().toDoubleOrNull() ?: 0.0
        val dollarValue = inputAmount * currentPriceUsd
        dollarValueTextView.text = "$${String.format("%.2f", dollarValue)}"
    }

    private fun getSelectedInterval(): String {
        val intervalsMap = mapOf(
            "1д" to "1",
            "7д" to "7",
            "30д" to "30",
            "90д" to "90",
            "1г" to "365"
        )
        val intervalLabel = spinner.selectedItem.toString()
        return intervalsMap[intervalLabel] ?: "7"
    }

    private fun loadData() {
        val cryptoIdInput = cryptoInput.text.toString().trim().lowercase(Locale.ROOT)
        if (cryptoIdInput.isNotEmpty()) {
            currentCryptoId = cryptoIdInput
            cryptoName.text = cryptoIdInput.replaceFirstChar { it.uppercase() }
            val interval = getSelectedInterval()
            viewModel.getCoinDetails(cryptoIdInput)
            viewModel.getHistoricalData(cryptoIdInput, interval)
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
                currentCryptoId?.let { cryptoId ->
                    val interval = getSelectedInterval()
                    viewModel.getHistoricalData(cryptoId, interval)
                }
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
        if (entries.isEmpty()) return

        val firstY = entries.first().y
        val lastY = entries.last().y
        val isFalling = lastY < firstY

        val lineColorValue = if (isFalling) Color.parseColor("#FF3B30") else Color.parseColor("#0088FF") // красный или синий
        val fillColorValue = if (isFalling) Color.parseColor("#FFD6D6") else Color.parseColor("#D6EBFF") // розовый или голубой

        val dataSet = LineDataSet(entries, "Цена USD").apply {
            mode = LineDataSet.Mode.CUBIC_BEZIER
            color = lineColorValue
            lineWidth = 2f
            setDrawCircles(true)
            setCircleColor(lineColorValue)
            circleRadius = 4f
            setDrawCircleHole(false)
            setDrawValues(false)
            setDrawFilled(true)
            setFillColor(fillColorValue)
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