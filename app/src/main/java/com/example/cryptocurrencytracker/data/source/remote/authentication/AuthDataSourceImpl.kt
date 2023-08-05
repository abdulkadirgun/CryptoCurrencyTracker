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

    // todo
    override fun checkUserSignedOrNot() {
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
        //reload()
        }
    }



    // todo
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

    // todo
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