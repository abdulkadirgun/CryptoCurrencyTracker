package com.example.cryptocurrencytracker.domain.usecases.favourite

import com.example.cryptocurrencytracker.domain.model.CoinItem
import com.example.cryptocurrencytracker.domain.repository.CryptoCurrencyRepository
import com.example.cryptocurrencytracker.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavCoinsUseCase @Inject constructor(
    private val repository: CryptoCurrencyRepository
){
    suspend operator fun invoke( ): Flow<Resource<MutableList<CoinItem>>> {
        return repository.getFavCoins()
    }
}