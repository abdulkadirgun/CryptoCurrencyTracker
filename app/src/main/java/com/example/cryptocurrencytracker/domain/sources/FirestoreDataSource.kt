package com.example.cryptocurrencytracker.domain.sources

import com.example.cryptocurrencytracker.data.source.remote.firestore.FavoriteCoin
import com.example.cryptocurrencytracker.util.Resource
import kotlinx.coroutines.flow.Flow

interface FirestoreDataSource {

    suspend fun addUserFavouriteCoin(userId : String, favoriteCoin: FavoriteCoin) : Flow<Resource<Void>>
}