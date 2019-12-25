package com.alialfayed.facerecognition.viewmodel

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import com.alialfayed.facerecognition.repository.FirebaseHandler
import com.alialfayed.facerecognition.view.activity.InfoActivity
import com.alialfayed.facerecognition.view.activity.SignUpActivity
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.activity_sign_up.*


/**
 * Class do :
 * Created by ( Eng Ali)
 */
class SignUpViewModel(val signUpActivity: SignUpActivity) : ViewModel() {


    // References of Firebase class -> this for connection to server
    private var firebaseHandler: FirebaseHandler = FirebaseHandler(signUpActivity, this)

    // check sign up
    fun signUpCheck(email: String, password: String) {
        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            Log.i("Signup","12")
            signUpActivity.disableLayout(false)
            firebaseHandler?.signUp(email, password)
        }
    }

    // if sign up Successful
    fun SignUpSuccessful() {
        Log.i("Signup","14")
        val email = signUpActivity.edtEmail_SignUp.text.toString()
        val start = Intent(signUpActivity, InfoActivity::class.java)
        start.putExtra("Email",email)
        signUpActivity.startActivity(start)
        signUpActivity.finish()

    }

    // if sign up failed
    fun SignUpfailed() {
        signUpActivity.disableLayout(true)
        val meg = "Sorry Sign Up Failed , Please try again"
        setMsgAlert(meg)
    }

    // check network
    fun isNetworkConnected(): Boolean {
        val connectivityManager =
            signUpActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }

    // Dialog
    fun setMsgAlert(msg: String) {
        AlertDialog.Builder(signUpActivity)
            .setTitle("Error")
            .setMessage(msg)
            .setIcon(com.alialfayed.facerecognition.R.drawable.ic_cancel)
            .setPositiveButton("Ok") { _, _ -> }
            .show()
    }



}