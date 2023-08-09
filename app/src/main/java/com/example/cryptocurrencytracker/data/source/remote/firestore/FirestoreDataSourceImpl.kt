package com.example.cryptocurrencytracker.data.source.remote.firestore

import android.util.Log
import com.example.cryptocurrencytracker.domain.model.CoinItem
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
    override suspend fun addThisCoinToFav(userId : String, coin: CoinItem): Flow<Resource<Void>> {
        return flow {
            try{
                emit(Resource.Loading())

                val param = firebaseFirestore.collection("users").document(userId)
                    .collection("favourite_coins").document(coin.id)
                    .set(coin).await()

                emit(Resource.Success(param))
            }
            catch(e :Exception) {
                emit(Resource.Error(e.message))
            }
        }
    }
    override suspend fun deleteThisCoinFromFav(userId : String, coin: CoinItem): Flow<Resource<Void>> {
        return flow {
            try{
                emit(Resource.Loading())

                val param =  firebaseFirestore.collection("users").document(userId)
                    .collection("favourite_coins").document(coin.id).delete().await()

                emit(Resource.Success(param))
            }
            catch(e :Exception) {
                emit(Resource.Error(e.message))
            }
        }
    }
     override suspend fun getFavCoins(userId : String): Flow<Resource<MutableList<CoinItem>>> {
        return flow {
            try{
                emit(Resource.Loading())

                val param = firebaseFirestore.collection("users").document(userId)
                    .collection("favourite_coins").get().await()

                val data = param.toObjects(CoinItem::class.java)
                emit(Resource.Success(data))
            }
            catch(e :Exception) {
                emit(Resource.Error(e.message))
            }
        }
    }

    override suspend fun checkCoinIsInFavList(userId : String, coin: CoinItem): Flow<Resource<Boolean>> {
        return flow {
            try{
                emit(Resource.Loading())

                val param = firebaseFirestore.collection("users").document(userId)
                    .collection("favourite_coins").get().await()

                val data = param.toObjects(CoinItem::class.java)
                val isInList = data.any { it.id == coin.id }

                emit(Resource.Success(isInList))
            }
            catch(e :Exception) {
                emit(Resource.Error(e.message))
            }
        }
    }


}

