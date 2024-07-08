package com.tradesave.list.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tradesave.list.database.data.TradeSaveEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TradeSaveDao {

    @Query("SELECT * FROM TradeSaveEntity")
    fun getAll(): Flow<List<TradeSaveEntity>>

    @Query("SELECT * FROM TradeSaveEntity WHERE id=:id")
    fun get(id: String): Flow<TradeSaveEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(values: List<TradeSaveEntity>)

    @Update
    suspend fun update(toUpdate: TradeSaveEntity)

    @Delete
    suspend fun delete(delete: TradeSaveEntity)

    @Query("DELETE FROM TradeSaveEntity WHERE id=:id")
    suspend fun delete(id: String)

    @Query("DELETE FROM TradeSaveEntity")
    suspend fun nukeTable()

}