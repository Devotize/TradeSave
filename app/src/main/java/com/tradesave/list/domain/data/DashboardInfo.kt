package com.tradesave.list.domain.data

import java.math.BigDecimal

class DashboardInfo(
    val overall: BigDecimal,
    val todayPercents: Double,
) {
    companion object {
        fun empty() = DashboardInfo(overall = BigDecimal(0), todayPercents = 0.0)
    }
}