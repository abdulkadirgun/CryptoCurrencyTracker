package com.example.cryptocurrencytracker.data.source.remote.crypto_api

import android.util.Log
import com.example.cryptocurrencytracker.data.source.remote.crypto_api.dto.CoinDetailItem
import com.example.cryptocurrencytracker.data.source.remote.crypto_api.dto.CoinItem
import com.example.cryptocurrencytracker.domain.sources.CryptoApiDataSource
import com.example.cryptocurrencytracker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class CryptoApiDataSourceImpl(
    private val cryptoApi: CryptoApi
) : CryptoApiDataSource {


    override suspend fun getCoinList(): Flow<Resource<List<CoinItem>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val coinList = cryptoApi.getCoinList()
                Log.d("CryptoApiDataSource", "coinList:${coinList.body()!!} ")
                emit(Resource.Success(coinList.body()!!))
            }
            catch (e: IOException){
                emit(Resource.Error("please check your internet connection"))
            }
            catch (e: Exception){
                emit(Resource.Error(e.localizedMessage ?: "an error was occurred"))
            }
        }
    }

    override suspend fun getCoinById(coinId: String): Flow<Resource<CoinDetailItem>> {
        return flow {
            emit(Resource.Loading())
            try {
                val coin = cryptoApi.getCoinById(coinId)
                Log.d("CryptoApiDataSource", "coin:${coin.body()!!} ")
                emit(Resource.Success(coin.body()!!))
            }
            catch (e: IOException){
                emit(Resource.Error("please check your internet connection"))
            }
            catch (e: Exception){
                emit(Resource.Error(e.localizedMessage ?: "an error was occurred"))
            }
        }    }
}