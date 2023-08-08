package com.example.cryptocurrencytracker.workmanager

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.cryptocurrencytracker.MainActivity
import com.example.cryptocurrencytracker.R
import com.example.cryptocurrencytracker.domain.model.CoinDetailItem
import com.example.cryptocurrencytracker.domain.model.CoinItem
import com.example.cryptocurrencytracker.domain.usecases.detail.GetCoinByIdUseCase
import com.example.cryptocurrencytracker.domain.usecases.favourite.AddThisCoinIntoUserFavUseCase
import com.example.cryptocurrencytracker.domain.usecases.favourite.DeleteThisCoinFromUserFavUseCase
import com.example.cryptocurrencytracker.domain.usecases.favourite.GetFavCoinsUseCase
import com.example.cryptocurrencytracker.domain.usecases.home.UpdateFavCoinsPriceUseCase
import com.example.cryptocurrencytracker.util.Constants
import com.example.cryptocurrencytracker.util.Resource
import com.example.cryptocurrencytracker.util.roundToTwoDecimal
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collect


@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val getFavCoinsUseCase: GetFavCoinsUseCase,
    private val getCoinByIdUseCase: GetCoinByIdUseCase,
    private val addThisCoinIntoUserFavUseCase: AddThisCoinIntoUserFavUseCase,
    ) : CoroutineWorker(context, workerParams)
{
    override suspend fun doWork(): Result {

        /**
        *  check price changes
        * */
        getFavCoinsUseCase().collect{ favCoins->
                when(favCoins){
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {

                        val priceChangedCoinList = arrayListOf<String>()

                        favCoins.data?.forEach { coinOld ->
                            getCoinByIdUseCase(coinId = coinOld.id).collect{ coinNew->
                                when(coinNew){
                                    is Resource.Error -> {}
                                    is Resource.Loading -> {}
                                    is Resource.Success -> {
                                        val oldPrice =  coinOld.current_price
                                        val newPrice =  coinNew.data?.market_data?.current_price?.usd?.roundToTwoDecimal()

                                        if (newPrice != oldPrice){
                                            priceChangedCoinList.add(coinOld.symbol)

                                            /**
                                             * sync data with backend
                                             * */
                                            addThisCoinIntoUserFavUseCase(coinNew.data!!.toCoinItem()).collect{
                                            }
                                            Log.d("NotificationService", "old price:$oldPrice new price:$newPrice")
                                        }
                                    }
                                }
                            }
                        }

                        if (priceChangedCoinList.isNotEmpty()) {
                            sendNotification(priceChangedCoinList)
                        }
                        else
                            Log.d("NotificationService", "the price of any coin has not changed")
                    }
                }
            }
        return Result.success()
    }

    private fun sendNotification(priceChangedCoinList: ArrayList<String>) {
        Log.d("NotificationService", "send notification called $priceChangedCoinList")

        val theIntent = Intent(context, MainActivity::class.java)

        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 1001, theIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setContentTitle("your fav coins' price changed")
            .setContentText(priceChangedCoinList.toString())
            .setSmallIcon(R.drawable.notification_icon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_SOUND)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        createNotificationChannel()
        notificationManager.notify(1, builder)
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel.
            val descriptionText = "channel_description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance)
            mChannel.description = descriptionText
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            val notificationManager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}