package com.example.cryptocurrencytracker.domain.sources

import com.example.cryptocurrencytracker.data.source.local.db.entities.CoinEntity
import kotlinx.coroutines.flow.Flow

interface RoomDataSource {

    suspend fun saveDataToDB(coinList: List<CoinEntity>)

    fun getDataFromDB() : Flow<List<CoinEntity>>

    suspend fun deleteAllData()
    fun getSearchResult(coinName: String): Flow<List<CoinEntity>>


}