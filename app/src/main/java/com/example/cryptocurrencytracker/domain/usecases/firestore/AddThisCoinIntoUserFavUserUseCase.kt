package com.example.cryptocurrencytracker.domain.usecases.firestore

import com.example.cryptocurrencytracker.data.source.remote.firestore.FavoriteCoin
import com.example.cryptocurrencytracker.domain.repository.CryptoCurrencyRepository
import javax.inject.Inject

class AddThisCoinIntoUserFavUserUseCase @Inject constructor(
    private val repository: CryptoCurrencyRepository
){
    suspend operator fun invoke(favoriteCoin: FavoriteCoin) {
        return repository.addThisCoinIntoUserFav(favoriteCoin)
    }
}