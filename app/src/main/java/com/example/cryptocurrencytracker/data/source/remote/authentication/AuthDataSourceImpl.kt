package com.example.cryptocurrencytracker.data.source.remote.authentication

import android.util.Log
import com.example.cryptocurrencytracker.domain.sources.AuthDataSource
import com.example.cryptocurrencytracker.util.Resource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class AuthDataSourceImpl(
    private val auth: FirebaseAuth
    ) : AuthDataSource {

    override suspend fun checkUserSignedOrNot(): Flow<Resource<Boolean>> {
        // Check if user is signed in (non-null) and update UI accordingly.
        return flow {
            emit(Resource.Loading())
            try {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    emit(Resource.Success(true))
                    Log.d("AuthDataSource", "checkUserSignedOrNot true")

                }
                else {
                    emit(Resource.Success(false))
                    Log.d("AuthDataSource", "checkUserSignedOrNot false")
                }
            }
            catch (e: Exception){
                emit(Resource.Error(e.message))
                Log.d("AuthDataSource", "checkUserSignedOrNot error")
            }
        }
    }
    override suspend fun logout(): Flow<Resource<Boolean>> {
        // Check if user is signed in (non-null) and update UI accordingly.
        return flow {
            emit(Resource.Loading())
            try {
                auth.signOut()
                emit(Resource.Success(true))
                Log.d("AuthDataSource", "checkUserSignedOrNot true")
            }
            catch (e: Exception){
                emit(Resource.Error(e.message))
                Log.d("AuthDataSource", "checkUserSignedOrNot error")
            }
        }
    }

    override suspend fun register(email : String, password: String): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            try {
                emit(Resource.Success(auth.createUserWithEmailAndPassword(email, password).await()))
                Log.d("AuthDataSource", "createUserWithEmail:success")
            }
            catch (e: Exception){
                emit(Resource.Error(e.message))
                Log.w("AuthDataSource", "createUserWithEmail:failure", e)
            }
        }
    }

    override suspend fun login(email : String, password: String): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            try {
                emit(Resource.Success(auth.signInWithEmailAndPassword(email, password).await()))
                Log.d("AuthDataSource", "signInWithEmail:success")
            }
            catch (e: Exception){
                emit(Resource.Error(e.message))
                Log.w("AuthDataSource", "signInWithEmail:failure", e)
            }
        }
    }

    override fun getUserId() : String{
        return auth.currentUser?.uid!!
    }
}