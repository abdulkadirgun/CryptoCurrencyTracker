package com.example.cryptocurrencytracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.cryptocurrencytracker.domain.repository.CryptoCurrencyRepository
import com.example.cryptocurrencytracker.domain.usecases.SyncAppDataWithBackendUseCase
import com.example.cryptocurrencytracker.domain.usecases.home.GetCoinsAndSaveDBUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val syncAppDataWithBackendUseCase: SyncAppDataWithBackendUseCase,

    ) : ViewModel() {

    fun scheduleWorksToSyncAppWithBackend(){
        syncAppDataWithBackendUseCase()
    }



}