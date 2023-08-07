package com.example.cryptocurrencytracker.domain.repository

import com.example.cryptocurrencytracker.data.source.local.db.entities.CoinEntity
import com.example.cryptocurrencytracker.domain.model.CoinDetailItem
import com.example.cryptocurrencytracker.domain.model.CoinItem
import com.example.cryptocurrencytracker.util.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface CryptoCurrencyRepository {

    //Room
    suspend fun saveDataToDB(coinList: List<CoinEntity>)
    fun getDataFromDB() : Flow<List<CoinEntity>>

    //Api
    suspend fun getCoinById(coinId: String) : Flow<Resource<CoinDetailItem>>

    //Api and Room
    suspend fun getCoinListFromRemoteAndSaveDB() : Flow<Resource<Boolean>>

    fun getSearchResult(coinName: String): Flow<List<CoinEntity>>


    //Auth
    suspend fun checkUserSignedOrNot(): Flow<Resource<Boolean>>
    suspend fun register(email : String, password: String) : Flow<Resource<AuthResult>>
    suspend fun login(email : String, password: String) : Flow<Resource<AuthResult>>

    suspend fun addThisCoinToFav(coin: CoinItem): Flow<Resource<Void>>
    suspend fun deleteThisCoinFromFav(coin: CoinItem): Flow<Resource<Void>>
    suspend fun getFavCoins(): Flow<Resource<MutableList<CoinItem>>>
    suspend fun checkCoinIsInFavList(coin: CoinItem): Flow<Resource<Boolean>>


}