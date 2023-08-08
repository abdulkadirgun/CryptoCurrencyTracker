package com.example.cryptocurrencytracker.domain.usecases.favourite

import android.util.Log
import com.example.cryptocurrencytracker.domain.model.CoinItem
import com.example.cryptocurrencytracker.domain.repository.CryptoCurrencyRepository
import com.example.cryptocurrencytracker.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddThisCoinIntoUserFavUseCase @Inject constructor(
    private val repository: CryptoCurrencyRepository
){
    suspend operator fun invoke(coinItem: CoinItem): Flow<Resource<Void>> {
        Log.d("NotificationService", "coinItem: $coinItem")
        return repository.addThisCoinToFav(coinItem)
    }
}