package com.example.cryptocurrencytracker.domain.repository

import com.example.cryptocurrencytracker.data.source.local.db.entities.CoinEntity
import com.example.cryptocurrencytracker.data.source.remote.crypto_api.dto.CoinDetailItem
import com.example.cryptocurrencytracker.data.source.remote.crypto_api.dto.CoinItem
import com.example.cryptocurrencytracker.data.source.remote.firestore.FavoriteCoin
import com.example.cryptocurrencytracker.util.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.Entity

interface CryptoCurrencyRepository {

    //Room
    suspend fun saveDataToDB(coinList: List<CoinEntity>)
    fun getDataFromDB() : Flow<List<CoinEntity>>

    //Api
    suspend fun getCoinById(coinId: String) : Flow<Resource<CoinDetailItem>>

    //Api and Room
    suspend fun getCoinList() : Flow<Resource<List<CoinEntity>>>

    //Auth
    fun checkUserSignedOrNot()
    suspend fun register(email : String, password: String) : Flow<Resource<AuthResult>>
    suspend fun login(email : String, password: String) : Flow<Resource<AuthResult>>


    suspend fun addThisCoinIntoUserFav(favoriteCoin: FavoriteCoin)


}