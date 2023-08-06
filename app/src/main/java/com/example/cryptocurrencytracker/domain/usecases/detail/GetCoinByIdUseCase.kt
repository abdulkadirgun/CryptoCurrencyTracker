package com.example.cryptocurrencytracker.domain.usecases.detail

import com.example.cryptocurrencytracker.domain.model.CoinDetailItem
import com.example.cryptocurrencytracker.domain.repository.CryptoCurrencyRepository
import com.example.cryptocurrencytracker.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoinByIdUseCase @Inject constructor(
    private val repository: CryptoCurrencyRepository
) {
    suspend operator fun invoke(coinId : String): Flow<Resource<CoinDetailItem>> {
        return repository.getCoinById(coinId)
    }
}