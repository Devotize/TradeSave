package com.tradesave.list.utils

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

fun BigDecimal.toLocalString(): String {
    val nf = NumberFormat.getNumberInstance(Locale.US)
    return nf.format(this)
}