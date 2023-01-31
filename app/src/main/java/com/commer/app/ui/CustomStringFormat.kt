package com.commer.app.ui

import java.text.DecimalFormat
import kotlin.math.abs

object CustomStringFormat {
    fun Int.toGlobalMoney(): String {
        val df = DecimalFormat("#.#")
        val number = this.toDouble()
        return when {
            abs(number / 1000000000) >= 1 -> df.format(number / 1000000000).toString() + "b"
            abs(number / 1000000) >= 1 -> df.format(number / 1000000).toString() + "m"
            abs(number / 1000) >= 1 -> df.format(number / 1000).toString() + "k"
            else -> number.toInt().toString()
        }
    }
}