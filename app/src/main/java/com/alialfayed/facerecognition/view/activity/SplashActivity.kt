package com.alialfayed.facerecognition.view.activity

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PowerManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.alialfayed.facerecognition.R
import com.alialfayed.facerecognition.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    /**
     * three methods
     * onCreate , durationOfWait , checkPermission
     * @author Ali Al Fayed
     */

    private val SPLASH_DISPLAY_LENGTH = 2300
    private lateinit var databaseUser: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        databaseUser = FirebaseDatabase.getInstance().getReference("User")
        durationOfWait()
    }

    @SuppressLint("BatteryLife")
    private fun durationOfWait() {
        Handler().postDelayed({
            /* Create an Intent that will start the Menu-Activity. */
            val mainIntent = Intent(this, SignInActivity::class.java)
            val userInternt = Intent(this, HomeActivity::class.java)
            val animation =
                android.util.Pair<View, String>(imgLogo, getString(R.string.logo_transition))
            val option = ActivityOptions.makeSceneTransitionAnimation(this, animation)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val packageName = packageName
                val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
                if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                    val intent = Intent()
                    intent.action = android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.data = Uri.parse("package:$packageName")
                    startActivity(intent)
                }
            }
            /**
             * Check currentUser
             */
            if (FirebaseAuth.getInstance().currentUser == null) {
                startActivity(mainIntent, option.toBundle())
            } else {

                if (FirebaseAuth.getInstance().currentUser!!.isEmailVerified) {

                    databaseUser.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            Log.i("splash", "2")
                            // get data from User Data
                            for (profileSnapShot in dataSnapshot.children) {
                                Log.i("splash", "3")
                                // get data from profileSnapShot Data kind of User
                                val profileDatabase = profileSnapShot.getValue(User::class.java)
                                Log.i("splash", "4")
                                // check if user in data
                                if (profileDatabase!!.getuserId() == FirebaseAuth.getInstance().currentUser!!.uid) {
                                    Log.i("splash", "5")
                                    // check if user have data
                                    if (profileDatabase.getvisibleData() == "true") {
                                        Log.i("splash", "6")
                                        // actions if true
                                        Toast.makeText( applicationContext, " Success ", Toast.LENGTH_LONG).show()
                                        startActivity(userInternt)
                                    } else {
                                        Log.i("splash", "7")
                                        goToInfo()
                                    }
                                    Log.i("splash", "8")
                                } else {
                                    goToInfo()
                                }
                                Log.i("splash", "9")
                                // no have childern all data
                            }
                            Log.i("splash", "10")
                        }
                    })
                }else{
                    startActivity(mainIntent, option.toBundle())
                }

            }
            this.finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())

    }

    fun goToInfo() {
        val start = Intent(this, InfoActivity::class.java)
        this.startActivity(start)
        this.finish()


    }

}
