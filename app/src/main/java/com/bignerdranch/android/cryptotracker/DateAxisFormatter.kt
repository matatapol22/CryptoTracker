package com.bignerdranch.android.cryptotracker

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateAxisFormatter : ValueFormatter() {
    private val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())

    override fun getFormattedValue(value: Float): String {
        val millis = value.toLong()
        return dateFormat.format(Date(millis))
    }
}