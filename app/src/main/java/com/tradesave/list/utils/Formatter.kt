package com.tradesave.list.utils

import java.text.DecimalFormat
import java.time.format.DateTimeFormatter

val TradeSaveDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
val TradeSaveTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

val PercentsFormat = DecimalFormat("#.00")