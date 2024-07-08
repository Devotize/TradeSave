package com.tradesave.list.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tradesave.list.database.data.TradeSaveEntity

@Database(entities = [TradeSaveEntity::class], version = 1)
abstract class TradeSaveDatabase : RoomDatabase() {
    abstract fun tradeSaveDao(): TradeSaveDao

    companion object {
        private var instance: TradeSaveDatabase? = null
        fun getInstance(applicationContext: Context): TradeSaveDatabase =
            synchronized(TradeSaveDatabase::class) {
                instance ?: Room.databaseBuilder(
                    applicationContext,
                    TradeSaveDatabase::class.java,
                    "trade_save_database"
                ).build().also { newDB ->
                    instance = newDB
                }
            }
    }

}