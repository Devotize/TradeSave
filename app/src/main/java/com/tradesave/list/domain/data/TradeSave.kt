package com.tradesave.list.domain.data

import androidx.annotation.StringRes
import com.tradesave.list.R
import com.tradesave.list.utils.isToday
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class TradeSave(
    val id: String,
    val positionType: PositionType,
    val assetName: String,
    val entry: TradePoint?,
    val exit: TradePoint?,
    val note: String?,
) {

    companion object {
        fun empty() = TradeSave(
            id = UUID.randomUUID().toString(),
            positionType = PositionType.Long,
            assetName = "",
            entry = TradePoint.empty(),
            exit = null,
            note = null
        )
    }

    fun getProfit(): BigDecimal {
        if (exit?.price == null ||
            entry?.price == null ||
            exit.quantity == null ||
            entry.quantity == null
        ) return BigDecimal(0.0)
        val extValue = exit.price
        val exitQty = exit.quantity
        val enterValue = entry.price
        val enterQty = entry.quantity
        return if (positionType == PositionType.Long) {
            (extValue * exitQty) - (enterValue * enterQty)
        } else {
            (enterValue * enterQty) - (extValue * exitQty)
        }
    }

    fun getPercents(): Double? {
        if (exit?.price == null ||
            entry?.price == null ||
            exit.quantity == null ||
            entry.quantity == null
        ) return null
        val exitValue = exit.price.toDouble()
        val exitQty = exit.quantity.toDouble()
        val enterValue = entry.price.toDouble()
        val enterQty = entry.quantity.toDouble()
        return if (positionType == PositionType.Long) {
            ((exitValue * exitQty) - (enterValue * enterQty)) / ((enterValue * enterQty) / 100)
        } else {
            ((enterValue * enterQty) - (exitValue * exitQty)) / ((exitQty * exitQty) / 100)
        }
    }

    fun getEntryValue(): BigDecimal {
        if (entry?.price == null || entry.quantity == null) return BigDecimal(0.0)
        return entry.price * entry.quantity
    }

    fun isToday(): Boolean {
        if (exit == null) return false
        return exit.date.isToday()
    }

    private val entryMustHave = listOf<Any?>(
        entry?.price,
        entry?.quantity,
        entry?.fees,
    )

    private val exitMustHave = listOf<Any?>(
        exit?.price,
        exit?.quantity,
        exit?.fees
    )

    fun isValidForSave() =
        when {
            exit == null -> {
                assetName.isNotEmpty() && !entryMustHave.contains(null)
            }

            else -> {
                assetName.isNotEmpty() &&
                        !entryMustHave.contains(null) &&
                        !exitMustHave.contains(null)
            }
        }

    fun getStatus(): Status {
        if (entry?.price == null || exit?.price == null) return Status.Undefined
        val diff = exit.price - entry.price
        return when {
            positionType == PositionType.Long -> {
                if (diff > BigDecimal(0.0)) {
                    Status.Profit
                } else {
                    Status.Loss
                }
            }
            else -> {
                if (diff < BigDecimal(0.0)) {
                    Status.Profit
                } else {
                    Status.Loss
                }
            }
        }
    }

}

data class TradePoint(
    val date: LocalDate,
    val time: LocalTime,
    val price: BigDecimal?,
    val quantity: BigDecimal?,
    val fees: BigDecimal?,
) {
    companion object {
        fun empty() = TradePoint(
            date = LocalDate.now(),
            time = LocalTime.now(),
            price = null,
            quantity = null,
            fees = null,
        )
    }
}

enum class PositionType(@StringRes val stringRes: Int) {
    Long(R.string.long_text), Short(R.string.short_text), // Order matters
}

enum class Status {
    Profit, Loss, Undefined
}