package com.example.cryptocurrencytracker.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencytracker.domain.usecases.auth.RegisterUserUseCase
import com.example.cryptocurrencytracker.util.Resource
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
): ViewModel() {

    private val _registerInfo: MutableStateFlow<Resource<AuthResult>?> = MutableStateFlow(null)
    val registerInfo = _registerInfo.asStateFlow()


    fun registerUser(email :String, password :String){
        viewModelScope.launch {
            registerUserUseCase(email, password).collect{ result->
                _registerInfo.update { result }
            }
        }
    }

}