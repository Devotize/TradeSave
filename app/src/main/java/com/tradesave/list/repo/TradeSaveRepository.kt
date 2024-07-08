package com.tradesave.list.repo

import com.tradesave.list.database.TradeSaveDatabase
import com.tradesave.list.database.data.TradeSaveEntity
import com.tradesave.list.domain.data.PositionType
import com.tradesave.list.domain.data.TradePoint
import com.tradesave.list.domain.data.TradeSave
import com.tradesave.list.utils.TradeSaveDateFormatter
import com.tradesave.list.utils.TradeSaveTimeFormatter
import com.tradesave.list.utils.mapList
import com.tradesave.list.utils.toLocalDate
import com.tradesave.list.utils.toLocalTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class TradeSaveRepository(
    private val db: TradeSaveDatabase,
) {

    private val dao by lazy { db.tradeSaveDao() }

    suspend fun saveToDb(model: TradeSave) {
        dao.insert(listOf(model.let(::modelToDb)))
    }

    fun getSavedTrades() =
        dao.getAll().mapList { it.let(::dbToModel) }

    suspend fun updateTradeSave(model: TradeSave) {
        dao.update(model.let(::modelToDb))
    }

    suspend fun deleteAllTrades() {
        dao.nukeTable()
    }

    suspend fun deleteTrade(id: String) {
        dao.delete(id)
    }

    fun getTradeSave(id: String): Flow<Result<TradeSave>> =
        dao.get(id).map { Result.success(it.let(::dbToModel)) }
            .catch { Result.failure<TradeSave>(it) }

    private fun modelToDb(model: TradeSave): TradeSaveEntity = with(model) {
        TradeSaveEntity(
            id = id,
            positionType = positionType.ordinal,
            assetName = assetName,
            entryDate = entry?.date?.format(TradeSaveDateFormatter),
            entryTime = entry?.time?.format(TradeSaveTimeFormatter),
            entryPrice = entry?.price?.toString(),
            entryQuantity = entry?.quantity?.toString(),
            entryFees = entry?.fees?.toString(),
            extDate = exit?.date?.format(TradeSaveDateFormatter),
            extTime = exit?.time?.format(TradeSaveTimeFormatter),
            extPrice = exit?.price?.toString(),
            extQuantity = exit?.quantity?.toString(),
            extFees = exit?.fees?.toString(),
            note = note
        )
    }

    private fun dbToModel(db: TradeSaveEntity): TradeSave = with(db) {
        TradeSave(
            id = id,
            positionType = PositionType.entries[positionType],
            assetName = assetName,
            entry = if (entryDate != null && entryTime != null) {
                TradePoint(
                    date = entryDate.toLocalDate(),
                    time = entryTime.toLocalTime(),
                    price = entryPrice?.toBigDecimal(),
                    quantity = entryQuantity?.toBigDecimal(),
                    fees = entryFees?.toBigDecimal(),
                )
            } else {
                null
            },
            exit = if (
                extDate != null && extTime != null
            ) {
                TradePoint(
                    date = extDate.toLocalDate(),
                    time = extTime.toLocalTime(),
                    price = extPrice?.toBigDecimal(),
                    quantity = extQuantity?.toBigDecimal(),
                    fees = extFees?.toBigDecimal(),
                )
            } else {
                null
            },
            note = note
        )
    }

}