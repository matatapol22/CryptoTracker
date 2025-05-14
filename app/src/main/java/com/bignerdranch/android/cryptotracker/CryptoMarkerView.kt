package com.bignerdranch.android.cryptotracker

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

class CryptoMarkerView(context: Context, layoutResource: Int) :
    MarkerView(context, layoutResource) {

    private val priceText: TextView = findViewById(R.id.markerPrice)

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        priceText.text = "$${e?.y}"
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2).toFloat(), -height.toFloat())
    }
}