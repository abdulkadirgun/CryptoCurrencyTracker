package com.example.cryptocurrencytracker.data.repository

import android.util.Log
import com.example.cryptocurrencytracker.data.source.local.db.entities.CoinEntity
import com.example.cryptocurrencytracker.data.source.remote.crypto_api.dto.CoinDetailItem
import com.example.cryptocurrencytracker.data.source.remote.crypto_api.dto.CoinItem
import com.example.cryptocurrencytracker.data.source.remote.firestore.FavoriteCoin
import com.example.cryptocurrencytracker.domain.repository.CryptoCurrencyRepository
import com.example.cryptocurrencytracker.domain.sources.AuthDataSource
import com.example.cryptocurrencytracker.domain.sources.CryptoApiDataSource
import com.example.cryptocurrencytracker.domain.sources.FirestoreDataSource
import com.example.cryptocurrencytracker.domain.sources.RoomDataSource
import com.example.cryptocurrencytracker.util.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.lang.Exception


class CryptoCurrencyRepositoryImpl(
    private val roomDataSource: RoomDataSource,
    private val cryptoApiDataSource: CryptoApiDataSource,
    private val authDataSource: AuthDataSource,
    private val firestoreDataSource: FirestoreDataSource
) :CryptoCurrencyRepository {

    override suspend fun saveDataToDB(coinList: List<CoinEntity>) { roomDataSource.saveDataToDB(coinList) }

    override fun getDataFromDB() = roomDataSource.getDataFromDB()

    override suspend fun getCoinList(): Flow<Resource<List<CoinEntity>>> {

        return flow {
            emit(Resource.Loading())

                val coins = cryptoApiDataSource.getCoinList()
                coins.collect { coinsResource ->
                    when (coinsResource) {
                        is Resource.Error ->  emit(Resource.Error(coinsResource.message))
                        is Resource.Loading -> emit(Resource.Loading())
                        is Resource.Success -> {
                            Log.d("CryptoCurrencyRepository", "api'den başarıyla çekildi")
                            roomDataSource.deleteAllData()
                            coinsResource.data?.let { coinList ->
                                roomDataSource.saveDataToDB(coinList.map { it.toEntity() })
                            }
                            Log.d("CryptoCurrencyRepository", "db'ye kaydedildi")
                            val dbData = roomDataSource.getDataFromDB()
                            dbData.collect { coinsDB ->
                                emit(Resource.Success(coinsDB))
                                Log.d("CryptoCurrencyRepository", "coinsDB emit ediliyor")
                            }
                        }
                    }
                }
        }
    }

    override suspend fun getCoinById(coinId : String) = cryptoApiDataSource.getCoinById(coinId)


    override fun checkUserSignedOrNot() {

    }

    override suspend fun register(email: String, password: String): Flow<Resource<AuthResult>> {
        return authDataSource.register(email, password)
    }

    override suspend fun login(email: String, password: String): Flow<Resource<AuthResult>> {
        return authDataSource.login(email, password)
    }

    override suspend fun addThisCoinIntoUserFav(favoriteCoin: FavoriteCoin) {
        val mUserId = authDataSource.getUserId()
        firestoreDataSource.addUserFavouriteCoin(userId = mUserId, favoriteCoin = favoriteCoin)
    }

}