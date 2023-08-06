package com.example.cryptocurrencytracker.domain.usecases.favourite

import com.example.cryptocurrencytracker.domain.model.CoinItem
import com.example.cryptocurrencytracker.domain.repository.CryptoCurrencyRepository
import com.example.cryptocurrencytracker.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckThisCoinIsInFavListUseCase @Inject constructor(
    private val repository: CryptoCurrencyRepository
){
    suspend operator fun invoke(coin : CoinItem): Flow<Resource<Boolean>> {
        return repository.checkCoinIsInFavList(coin)
    }
}