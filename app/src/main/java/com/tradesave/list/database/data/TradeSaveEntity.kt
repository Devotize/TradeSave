package com.tradesave.list.database.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class TradeSaveEntity(
    @PrimaryKey val id: String,
    val positionType: Int,
    val assetName: String,
    val entryDate: String?,
    val entryTime: String?,
    val entryPrice: String?,
    val entryQuantity: String?,
    val entryFees: String?,
    val extDate: String?,
    val extTime: String?,
    val extPrice: String?,
    val extQuantity: String?,
    val extFees: String?,
    val note: String?,
)