package com.tradesave.list.controller

import android.content.Context
import com.tradesave.list.app.TradeSaveApplication
import com.tradesave.list.database.TradeSaveDatabase
import com.tradesave.list.domain.data.DashboardInfo
import com.tradesave.list.domain.data.TradePoint
import com.tradesave.list.domain.data.TradeSave
import com.tradesave.list.repo.TradeSaveRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.math.BigDecimal

class GlobalStateController private constructor(appContext: Context) {

    private val scope = CoroutineScope(SupervisorJob())

    private val repository = TradeSaveRepository(TradeSaveDatabase.getInstance(appContext))

    val traderId by lazy {
        (0..1_000_000_000).random()
    }

    val savedTradesStream = repository.getSavedTrades().map { it.reversed() }

    val dashboardInfoStream =
        repository.getSavedTrades().map { savedTrades ->
            val overall = savedTrades.getProfitOverall()
            val profitToday = savedTrades.getProfitToday()
            val profitNotToday = savedTrades.getProfitNotoday()
            val percentsForToday = if (profitToday.toDouble() == 0.0) {
                0.0
            } else {
                (profitNotToday.toDouble() / profitToday.toDouble()) * 100
            }
            DashboardInfo(
                overall = overall,
                todayPercents = percentsForToday
            )
        }

    private val _editTrade = MutableStateFlow<TradeSave?>(null)
    val editTrade = _editTrade.asStateFlow()

    fun getTradeSave(id: String): Flow<TradeSave?> =
        repository.getTradeSave(id).map { it.getOrNull() }

    fun processEvent(event: Event) {
        when (event) {
            is ChangePositionType -> {
                _editTrade.value = editTrade.value?.copy(
                    positionType = event.positionType
                )
            }

            is ChangeAssetName -> {
                _editTrade.value = editTrade.value?.copy(
                    assetName = event.name
                )
            }

            is ChangeEntryDate -> {
                _editTrade.value = editTrade.value?.copy(
                    entry = editTrade.value?.entry?.copy(
                        date = event.date
                    )
                )
            }

            is ChangeEntryTime -> {
                _editTrade.value = editTrade.value?.copy(
                    entry = editTrade.value?.entry?.copy(
                        time = event.time
                    )
                )
            }

            is ChangeEntryPrice -> {
                _editTrade.value = editTrade.value?.copy(
                    entry = editTrade.value?.entry?.copy(
                        price = event.value
                    )
                )
            }

            is ChangeEntryQuantity -> {
                _editTrade.value = editTrade.value?.copy(
                    entry = editTrade.value?.entry?.copy(
                        quantity = event.quantity
                    )
                )
            }

            is ChangeEntryFees -> {
                _editTrade.value = editTrade.value?.copy(
                    entry = editTrade.value?.entry?.copy(
                        fees = event.fees
                    )
                )
            }

            is ChangeExitDate -> {
                _editTrade.value = editTrade.value?.copy(
                    exit = editTrade.value?.exit?.copy(
                        date = event.date
                    )
                )
            }

            is ChangeExitTime -> {
                _editTrade.value = editTrade.value?.copy(
                    exit = editTrade.value?.exit?.copy(
                        time = event.time
                    )
                )
            }

            is ChangeExitPrice -> {
                _editTrade.value = editTrade.value?.copy(
                    exit = editTrade.value?.exit?.copy(
                        price = event.value
                    )
                )
            }

            is ChangeExitQuantity -> {
                _editTrade.value = editTrade.value?.copy(
                    exit = editTrade.value?.exit?.copy(
                        quantity = event.quantity
                    )
                )
            }

            is ChangeExitFees -> {
                _editTrade.value = editTrade.value?.copy(
                    exit = editTrade.value?.exit?.copy(
                        fees = event.fees
                    )
                )
            }

            is RemoveExit -> {
                _editTrade.value = editTrade.value?.copy(
                    exit = null,
                )
            }

            is AddExit -> {
                _editTrade.value = editTrade.value?.copy(
                    exit = TradePoint.empty()
                )
            }

            is SaveTrade -> {
                scope.launch(Dispatchers.IO) {
                    _editTrade.value?.let {
                        repository.saveToDb(it)
                        _editTrade.value = null
                    }
                }
            }

            is WriteReview -> {
                // TODO
            }

            is DeleteAllTrades -> {
                scope.launch(Dispatchers.IO) {
                    repository.deleteAllTrades()
                }
            }

            is DeleteTrade -> {
                scope.launch(Dispatchers.IO) {
                    repository.deleteTrade(event.id)
                }
            }

            is SetTradeToEdit -> {
                _editTrade.value = event.tradeSave
            }

            is ChangeNote -> {
                _editTrade.value = _editTrade.value?.copy(
                    note = event.note
                )
            }
        }
    }

    fun createNewTrade() {
        _editTrade.value = TradeSave.empty()
    }

    private fun List<TradeSave>.getProfitOverall(): BigDecimal {
        var profit = BigDecimal(0)
        forEach {
            profit += it.getProfit()
        }
        return profit
    }

    private fun List<TradeSave>.getProfitToday(): BigDecimal {
        var profit = BigDecimal(0)
        filter { it.isToday() }.forEach {
            profit += it.getProfit()
        }
        return profit
    }

    private fun List<TradeSave>.getProfitNotoday(): BigDecimal {
        var profit = BigDecimal(0)
        filter { !it.isToday() }.forEach {
            profit += it.getProfit()
        }
        return profit
    }

    companion object {
        private var instance: GlobalStateController? = null
        fun create(app: TradeSaveApplication) {
            instance = GlobalStateController(app.applicationContext)
        }

        fun getInstance() = instance ?: error("Must call create() first!")
    }

}