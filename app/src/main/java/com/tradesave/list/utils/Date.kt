package com.tradesave.list.utils

import java.time.LocalDate
import java.time.LocalTime

fun String.toLocalDate(): LocalDate =
    LocalDate.parse(this, TradeSaveDateFormatter)

fun String.toLocalTime(): LocalTime =
    LocalTime.parse(this, TradeSaveTimeFormatter)


fun LocalDate.isToday(): Boolean = this.isEqual(LocalDate.now())