package com.example.cryptocurrencytracker.data.source.remote.firestore

import com.example.cryptocurrencytracker.domain.sources.FirestoreDataSource
import com.example.cryptocurrencytracker.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class FirestoreDataSourceImpl(
    private val firebaseFirestore: FirebaseFirestore
    ) : FirestoreDataSource {
    override suspend fun addUserFavouriteCoin(userId : String, favoriteCoin: FavoriteCoin): Flow<Resource<Void>> {
        return flow {
            try{
                emit(Resource.Loading())

                val coinRef = firebaseFirestore.collection("users").document(userId)
                    .collection("coins").document("coinId")
                    .set(favoriteCoin).await()

                emit(Resource.Success(coinRef))
            }
            catch(e :Exception) {
                emit(Resource.Error(e.message))
            }
        }


    }

}
data class FavoriteCoin(
    var coinId: String? = "",
    val name: String? = "",
    val symbol: String? = "",
    val img: String? = "",
    val currentPrice: Double? = 0.0
)
