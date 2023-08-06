package com.example.cryptocurrencytracker.domain.usecases.home

import com.example.cryptocurrencytracker.data.source.local.db.entities.CoinEntity
import com.example.cryptocurrencytracker.domain.repository.CryptoCurrencyRepository
import com.example.cryptocurrencytracker.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoinListUseCase @Inject constructor(
    private val repository: CryptoCurrencyRepository
) {

    suspend operator fun invoke(): Flow<Resource<List<CoinEntity>>> {
        return repository.getCoinList()

    }
}