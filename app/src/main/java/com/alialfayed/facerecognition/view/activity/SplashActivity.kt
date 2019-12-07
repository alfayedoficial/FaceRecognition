package com.alialfayed.facerecognition.view.activity

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.alialfayed.facerecognition.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    /**
     * three methods
     * onCreate , durationOfWait , checkPermission
     * @author Ali Al Fayed
     */

    private val SPLASH_DISPLAY_LENGTH = 2300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        durationOfWait()
    }

    private fun durationOfWait() {
        Handler().postDelayed({
            /* Create an Intent that will start the Menu-Activity. */
            val mainIntent = Intent(this, SignInActivity::class.java)
            val userInternt = Intent(this, HomeActivity::class.java)
            val animation =
                android.util.Pair<View, String>(imgLogo, getString(R.string.logo_transition))
            val option = ActivityOptions.makeSceneTransitionAnimation(this, animation)

            /**
             * Check currentUser
             */
            if (FirebaseAuth.getInstance().currentUser == null) {
                startActivity(mainIntent, option.toBundle())
            } else {
                startActivity(userInternt)
            }
            this.finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())

    }
}
