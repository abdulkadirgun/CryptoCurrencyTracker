package com.example.cryptocurrencytracker.domain.usecases

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.cryptocurrencytracker.workmanager.NotificationWorker
import com.example.cryptocurrencytracker.util.Constants
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SyncAppDataWithBackendUseCase @Inject constructor(
    private val workManager: WorkManager
) {
    operator fun invoke(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            repeatInterval = Constants.SYNC_WITH_BACKEND_INTERVAL,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        )
            .setInitialDelay(15, TimeUnit.MINUTES) // initial delay
            .setConstraints(constraints)
            .build()

        /** cancel works in each app open */
        workManager.cancelAllWork()

        // Schedule the work
        workManager.enqueueUniquePeriodicWork(
            "NotificationWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }

}