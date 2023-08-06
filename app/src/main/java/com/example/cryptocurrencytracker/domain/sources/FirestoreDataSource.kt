package com.example.cryptocurrencytracker.domain.sources

import com.example.cryptocurrencytracker.domain.model.CoinItem
import com.example.cryptocurrencytracker.util.Resource
import kotlinx.coroutines.flow.Flow

interface FirestoreDataSource {

    suspend fun addThisCoinToFav(userId : String, coin: CoinItem): Flow<Resource<Void>>
    suspend fun deleteThisCoinFromFav(userId : String, coin: CoinItem): Flow<Resource<Void>>
    suspend fun getFavCoins(userId : String): Flow<Resource<MutableList<CoinItem>>>
    suspend fun checkCoinIsInFavList(userId : String, coin: CoinItem): Flow<Resource<Boolean>>
}