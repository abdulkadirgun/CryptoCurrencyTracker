package com.example.cryptocurrencytracker.data.repository

import android.util.Log
import com.example.cryptocurrencytracker.data.source.local.db.entities.CoinEntity
import com.example.cryptocurrencytracker.domain.model.CoinItem
import com.example.cryptocurrencytracker.domain.repository.CryptoCurrencyRepository
import com.example.cryptocurrencytracker.domain.sources.AuthDataSource
import com.example.cryptocurrencytracker.domain.sources.CryptoApiDataSource
import com.example.cryptocurrencytracker.domain.sources.FirestoreDataSource
import com.example.cryptocurrencytracker.domain.sources.RoomDataSource
import com.example.cryptocurrencytracker.util.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class CryptoCurrencyRepositoryImpl(
    private val roomDataSource: RoomDataSource,
    private val cryptoApiDataSource: CryptoApiDataSource,
    private val authDataSource: AuthDataSource,
    private val firestoreDataSource: FirestoreDataSource
) :CryptoCurrencyRepository {

    override suspend fun saveDataToDB(coinList: List<CoinEntity>) { roomDataSource.saveDataToDB(coinList) }

    override fun getDataFromDB() = roomDataSource.getDataFromDB()

    override suspend fun getCoinListFromRemoteAndSaveDB(): Flow<Resource<Boolean>> {

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
                            emit(Resource.Success(true))
                            Log.d("CryptoCurrencyRepository", "db'ye kaydedildi")
                           /* val dbData = roomDataSource.getDataFromDB()
                            dbData.collect { coinsDB ->
                                emit(Resource.Success(coinsDB))
                                Log.d("CryptoCurrencyRepository", "coinsDB emit ediliyor")
                            }*/
                        }
                    }
                }
        }
    }

    override fun getSearchResult(coinName: String): Flow<List<CoinEntity>> {
        return roomDataSource.getSearchResult(coinName)
    }

    override suspend fun getCoinById(coinId : String) = cryptoApiDataSource.getCoinById(coinId)


    override suspend fun checkUserSignedOrNot(): Flow<Resource<Boolean>> {
        return authDataSource.checkUserSignedOrNot()
    }

    override suspend fun register(email: String, password: String): Flow<Resource<AuthResult>> {
        return authDataSource.register(email, password)
    }

    override suspend fun login(email: String, password: String): Flow<Resource<AuthResult>> {
        return authDataSource.login(email, password)
    }

    override suspend fun addThisCoinToFav(coin: CoinItem): Flow<Resource<Void>> {
        val mUserId = authDataSource.getUserId()
        return firestoreDataSource.addThisCoinToFav(userId = mUserId, coin = coin)
    }

    override suspend fun deleteThisCoinFromFav(coin: CoinItem): Flow<Resource<Void>> {
        val mUserId = authDataSource.getUserId()
        return firestoreDataSource.deleteThisCoinFromFav(userId = mUserId, coin = coin)
    }

    override suspend fun getFavCoins(): Flow<Resource<MutableList<CoinItem>>> {
        val mUserId = authDataSource.getUserId()
        return firestoreDataSource.getFavCoins(userId = mUserId)
    }

    override suspend fun checkCoinIsInFavList(coin: CoinItem): Flow<Resource<Boolean>> {
        val mUserId = authDataSource.getUserId()
        return firestoreDataSource.checkCoinIsInFavList(userId = mUserId, coin = coin)
    }
}