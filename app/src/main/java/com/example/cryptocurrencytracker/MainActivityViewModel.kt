package com.example.cryptocurrencytracker

import androidx.lifecycle.ViewModel
import com.example.cryptocurrencytracker.domain.repository.CryptoCurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val cryptoCurrencyRepository: CryptoCurrencyRepository
) : ViewModel() {



}