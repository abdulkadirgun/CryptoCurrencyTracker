package com.example.cryptocurrencytracker.domain.usecases.home

import com.example.cryptocurrencytracker.domain.repository.CryptoCurrencyRepository
import com.example.cryptocurrencytracker.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoinsAndSaveDBUseCase @Inject constructor(
    private val repository: CryptoCurrencyRepository
) {

    suspend operator fun invoke(): Flow<Resource<Boolean>> {
        return repository.getCoinListFromRemoteAndSaveDB()

    }
}