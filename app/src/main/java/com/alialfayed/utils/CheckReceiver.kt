package com.alialfayed.deersms.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.widget.Toast
import com.alialfayed.facerecognition.model.PatientModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/**
 * Class do : this class to make service work in background if app close
 * Created by ( Eng Ali)
 */
class CheckReceiver : BroadcastReceiver() {

   lateinit var databaseReferenceMsg: DatabaseReference

    companion object {
        private const val REQUEST_TIMER1 = 1
        private var requestId: String = "1"

        fun getIntent(context: Context, requestCode: Int): PendingIntent? {
            val intent = Intent(context, CheckReceiver::class.java)
            return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        }

        // start service worked
        @SuppressLint("ShortAlarm")
        fun startAlarm(context: Context) {
            val pendingIntent = getIntent(context, REQUEST_TIMER1)
            val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 500, pendingIntent)
        }
        // Cancel service worked
        fun cancelAlarm(context: Context) {
            val pendingIntent = getIntent(context, REQUEST_TIMER1)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
        }
    }


    override fun onReceive(context: Context?, intent: Intent?) {

        databaseReferenceMsg = FirebaseDatabase.getInstance().getReference("Database")

        Check(context)
        Log.i("Reciver" , "Reciver worked")
//        showNotification(context , "MMM" , "KKKK")

    }

    private fun Check(context: Context?){
        databaseReferenceMsg.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (patientSnapshot in dataSnapshot.children) {
                    if (patientSnapshot.hasChildren()) {
                        val patSnapShot = patientSnapshot.getValue(PatientModel::class.java)
                        if (patSnapShot!!.getuserId() == FirebaseAuth.getInstance().currentUser!!.uid) {
                            if (patSnapShot!!.getvisibleData() == "true") {
                                if (patSnapShot.getpatientStatus() == "Dangerous") {
                                    showNotification(context , "Patient in Dangerous " , "Call Emergency Number")
                                }
                            }
                        }
                    }
                }
            }
        })

    }

    private fun showNotification(context: Context?, message: String , megDec: String) {
        val notificationHelper = NotificationHelper(context!!, message , megDec)
        val notification = notificationHelper.getChannelNotification()
        notificationHelper.getManager().notify(requestId.hashCode(), notification.build())
    }



}
