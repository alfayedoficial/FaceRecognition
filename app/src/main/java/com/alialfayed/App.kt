package com.alialfayed

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.google.firebase.database.FirebaseDatabase

/**
 * Class do : this class to save data if work offline
 * Created by ( Eng Ali)
 */
class App:Application() {
companion object{
    val CHANNEL_ID = "SeviceOnBackground"
}

    override fun onCreate() {
        super.onCreate()
        /**
         * Offline Firebase
         */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        createNotificationChannel()

    }

    fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
           val serviceChannel  = NotificationChannel(
               CHANNEL_ID,
               "Service Channel",
               NotificationManager.IMPORTANCE_DEFAULT
           )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }



















}