package com.tradesave.list.controller

import com.tradesave.list.domain.data.PositionType
import com.tradesave.list.domain.data.TradeSave
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

sealed interface Event

class ChangePositionType(val positionType: PositionType) : Event

class ChangeAssetName(val name: String) : Event

class ChangeEntryDate(val date: LocalDate) : Event

class ChangeEntryTime(val time: LocalTime) : Event

class ChangeEntryPrice(val value: BigDecimal?) : Event

class ChangeEntryQuantity(val quantity: BigDecimal?) : Event

class ChangeEntryFees(val fees: BigDecimal?) : Event

class ChangeExitDate(val date: LocalDate) : Event

class ChangeExitTime(val time: LocalTime) : Event

class ChangeExitPrice(val value: BigDecimal?) : Event

class ChangeExitQuantity(val quantity: BigDecimal?) : Event

class ChangeExitFees(val fees: BigDecimal?) : Event

class DeleteTrade(val id: String) : Event

class SetTradeToEdit(val tradeSave: TradeSave) : Event

class ChangeNote(val note: String?): Event

data object RemoveExit : Event

data object AddExit : Event

data object SaveTrade : Event

data object WriteReview : Event

data object DeleteAllTrades : Event