package com.alialfayed.facerecognition.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alialfayed.facerecognition.R
import kotlinx.android.synthetic.main.activity_add_patient.*
import kotlinx.android.synthetic.main.activity_policy.*

class PolicyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_policy)

        onClickNav()
    }

    private fun onClickNav(){
        /**
         * Actions
         */
        btnHome_policy.setOnClickListener {
            val startActivity = Intent(this,HomeActivity::class.java)
            startActivity(startActivity)
            finish()

        }
        btnPatient_policy.setOnClickListener {
            val startActivity = Intent(this,AddPatientActivity::class.java)
            startActivity(startActivity)
            finish()

        }
        btnAddNumber_policy.setOnClickListener {
            val startActivity = Intent(this,AddNumberCallActivity::class.java)
            startActivity(startActivity)
            finish()

        }
        btnProfile_policy.setOnClickListener {
            val startActivity = Intent(this,ProfileActivity::class.java)
            startActivity(startActivity)
            finish()

        }
        btnPolicy_policy.setOnClickListener {
            val startActivity = Intent(this,PolicyActivity::class.java)
            startActivity(startActivity)
            finish()

        }

    }
    override fun onBackPressed() {
        finish()
    }
}
