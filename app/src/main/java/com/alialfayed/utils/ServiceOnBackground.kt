package com.alialfayed.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.alialfayed.App.Companion.CHANNEL_ID
import com.alialfayed.facerecognition.R
import com.alialfayed.facerecognition.model.PatientModel
import com.alialfayed.facerecognition.view.activity.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.NonCancellable.start

/**
 * Class do :
 * Created by ( Eng Ali)
 */
class ServiceOnBackground: Service() {

    private  var mdatabaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("Database")

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mdatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChild("")){
                    for (patSnapshot in p0.children){
                        val patPatient = patSnapshot.getValue(PatientModel::class.java)
                        if (patPatient!!.getuserId() == FirebaseAuth.getInstance().currentUser!!.uid){
                            if (patPatient.getpatientStatus() == "Dangerous"){
                                start()
                            }else {

                            }
                        }
                    }
                }
            }
        })
        return START_NOT_STICKY

    }

    fun start(){
        Log.i("Service","start notification")


        val notificationIntent = Intent(this, HomeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,0, notificationIntent, 0 )


        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getText(R.string.app_name))
            .setContentText(" You have an emergency situation ")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }



}