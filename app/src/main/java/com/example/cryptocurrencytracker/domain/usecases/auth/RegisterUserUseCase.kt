package com.example.cryptocurrencytracker.domain.usecases.auth

import com.example.cryptocurrencytracker.domain.repository.CryptoCurrencyRepository
import com.example.cryptocurrencytracker.util.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: CryptoCurrencyRepository
){
    suspend operator fun invoke(email: String, password : String): Flow<Resource<AuthResult>> {
        return repository.register(email, password)

    }
}