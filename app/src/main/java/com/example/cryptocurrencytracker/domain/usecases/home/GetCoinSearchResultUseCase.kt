package com.example.cryptocurrencytracker.domain.usecases.home

import com.example.cryptocurrencytracker.data.source.local.db.entities.CoinEntity
import com.example.cryptocurrencytracker.domain.repository.CryptoCurrencyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoinSearchResultUseCase @Inject constructor(
    private val repository: CryptoCurrencyRepository
) {

    operator fun invoke(coinName: String): Flow<List<CoinEntity>> {
        return repository.getSearchResult(coinName)

    }
}