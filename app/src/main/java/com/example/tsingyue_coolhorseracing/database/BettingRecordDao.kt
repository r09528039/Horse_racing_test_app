package com.example.tsingyue_coolhorseracing.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.tsingyue_coolhorseracing.model.BettingRecord

@Dao
interface BettingRecordDao {
    @Insert
    suspend fun insert(record: BettingRecord)

    @Query("SELECT * FROM betting_records ORDER BY timestamp DESC")
    fun getAllRecords(): LiveData<List<BettingRecord>>

    @Delete
    suspend fun delete(record: BettingRecord)

    @Query("DELETE FROM betting_records WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM betting_records")
    suspend fun deleteAll()

    @Query("SELECT SUM(winAmount - betAmountInTWD) FROM betting_records")
    suspend fun getTotalProfitLoss(): Double?

    @Query("SELECT MAX(balanceAfterBet) FROM betting_records")
    suspend fun getHighestBalance(): Double?
}