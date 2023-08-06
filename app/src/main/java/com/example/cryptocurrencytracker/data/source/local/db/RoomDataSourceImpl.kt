package com.example.cryptocurrencytracker.data.source.local.db

import android.util.Log
import com.example.cryptocurrencytracker.data.source.local.db.entities.CoinEntity
import com.example.cryptocurrencytracker.domain.sources.RoomDataSource
import kotlinx.coroutines.flow.Flow

class RoomDataSourceImpl (
    private val cryptoCurrencyDB: CryptoCurrencyDB
) : RoomDataSource {
    override suspend fun saveDataToDB(coinList: List<CoinEntity>) {
        Log.d("RoomDataSource", "veriler db'ye kaydediliyor")
        cryptoCurrencyDB.getDao().saveDataToDB(coinList)
    }

    override fun getDataFromDB(): Flow<List<CoinEntity>> {
        Log.d("RoomDataSource", "veriler db'den çekiliyor")
        return cryptoCurrencyDB.getDao().getDataFromDB()
    }

    override suspend fun deleteAllData() {
        Log.d("RoomDataSource", "tüm veriler silindi")
        cryptoCurrencyDB.getDao().deleteAllData()
    }

    override fun getSearchResult(coinName: String): Flow<List<CoinEntity>> {
        return cryptoCurrencyDB.getDao().getSearchResult(coinName)
    }

}