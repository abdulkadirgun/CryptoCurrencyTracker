package com.example.cryptocurrencytracker.domain.sources

import com.example.cryptocurrencytracker.util.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthDataSource {

    suspend fun checkUserSignedOrNot(): Flow<Resource<Boolean>>
    suspend fun logout(): Flow<Resource<Boolean>>
    suspend fun register(email : String, password: String) : Flow<Resource<AuthResult>>
    suspend fun login(email : String, password: String) : Flow<Resource<AuthResult>>
    fun getUserId() :String
}