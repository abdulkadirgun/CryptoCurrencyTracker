package com.example.cryptocurrencytracker.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencytracker.domain.usecases.auth.LoginUserUseCase
import com.example.cryptocurrencytracker.domain.usecases.auth.UserAlreadyLoggedInUseCase
import com.example.cryptocurrencytracker.util.Resource
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val userAlreadyLoggedInUseCase: UserAlreadyLoggedInUseCase

): ViewModel() {

    private val _loginInfo: MutableStateFlow<Resource<AuthResult>?> = MutableStateFlow(null)
    val loginInfo = _loginInfo.asStateFlow()

    private val _isUserAlsoLoggedIn: MutableStateFlow<Resource<Boolean>?> = MutableStateFlow(null)
    val isUserAlsoLoggedIn = _isUserAlsoLoggedIn.asStateFlow()


    fun loginUser(email :String, password :String){
        viewModelScope.launch {
            loginUserUseCase(email, password).collect{ result->
                _loginInfo.update { result }
            }
        }
    }

    fun checkUserLoggedIn(){
        viewModelScope.launch {
            userAlreadyLoggedInUseCase().collect{ result->
                _isUserAlsoLoggedIn.update { result }
            }
        }
    }


}