package com.example.cryptocurrencytracker.domain.usecases.home

import com.example.cryptocurrencytracker.data.source.local.db.entities.CoinEntity
import com.example.cryptocurrencytracker.domain.repository.CryptoCurrencyRepository
import com.example.cryptocurrencytracker.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoinsFromDBUseCase @Inject constructor(
    private val repository: CryptoCurrencyRepository
) {

    operator fun invoke(): Flow<List<CoinEntity>> {
        return repository.getDataFromDB()

    }
}