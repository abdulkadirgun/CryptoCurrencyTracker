package com.example.cryptocurrencytracker.data.source.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cryptocurrencytracker.data.source.local.db.entities.CoinEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CryptoCurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDataToDB(coinList: List<CoinEntity>)

    @Query("SELECT * FROM CoinEntity")
    fun getDataFromDB() : Flow<List<CoinEntity>>


    @Query("DELETE FROM CoinEntity")
    suspend fun deleteAllData()

    @Query("SELECT * FROM CoinEntity WHERE name LIKE '%' || :coinName || '%' OR name LIKE '%' || :coinName")
    fun getSearchResult(coinName: String): Flow<List<CoinEntity>>


}