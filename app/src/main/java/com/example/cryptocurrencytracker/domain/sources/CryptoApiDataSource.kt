package com.example.cryptocurrencytracker.domain.sources

import com.example.cryptocurrencytracker.domain.model.CoinDetailItem
import com.example.cryptocurrencytracker.domain.model.CoinItem
import com.example.cryptocurrencytracker.util.Resource
import kotlinx.coroutines.flow.Flow

interface CryptoApiDataSource {
    suspend fun getCoinList() : Flow<Resource<List<CoinItem>>>
    suspend fun getCoinById(coinId: String) : Flow<Resource<CoinDetailItem>>
}