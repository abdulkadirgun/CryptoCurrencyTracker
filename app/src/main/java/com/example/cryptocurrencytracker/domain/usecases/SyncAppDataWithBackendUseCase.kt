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
        // Create constraints (optional)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Create a repeating work request
        val repeatingRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            repeatInterval = Constants.SYNC_WITH_BACKEND_INTERVAL, // Minutes
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        )
            // todo
            .setInitialDelay(5, TimeUnit.SECONDS) // 5 minutes initial delay
            .setConstraints(constraints)
            .build()

        workManager.cancelAllWork()

        // Schedule the work
        workManager.enqueueUniquePeriodicWork(
            "MyWorkerTag",
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }

}