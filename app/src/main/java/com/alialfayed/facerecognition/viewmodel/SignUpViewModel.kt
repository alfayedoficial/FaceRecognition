package com.alialfayed.facerecognition.viewmodel

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
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


    private var firebaseHandler: FirebaseHandler = FirebaseHandler(signUpActivity, this)

    fun signUpCheck(email: String, password: String) {
        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            signUpActivity.disableLayout(false)
            firebaseHandler?.signUp(email, password)
        }
    }

    fun SignUpSuccessful() {
//        val meg = "Sign Up Successful , Please confirm the account and Sign In"
//        AlertDialog.Builder(signUpActivity)
//            .setTitle("SignUp Successful")
//            .setMessage(meg)
//            .setIcon(com.alialfayed.facerecognition.R.drawable.ic_successful)
//            .setPositiveButton("Ok") { _, _ ->
//                val start = Intent(signUpActivity, SignInActivity::class.java)
//                signUpActivity.startActivity(start)
//                signUpActivity.finish()
//            }
//            .show()
        val email = signUpActivity.edtEmail_SignUp.text.toString()
        val start = Intent(signUpActivity, InfoActivity::class.java)
        start.putExtra("Email",email)
        signUpActivity.startActivity(start)
        signUpActivity.finish()

    }

    fun SignUpfailed() {
        signUpActivity.disableLayout(true)
        val meg = "Sorry Sign Up Failed , Please try again"
        setMsgAlert(meg)
    }

    fun isNetworkConnected(): Boolean {
        val connectivityManager =
            signUpActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }

    fun setMsgAlert(msg: String) {
        AlertDialog.Builder(signUpActivity)
            .setTitle("Error")
            .setMessage(msg)
            .setIcon(com.alialfayed.facerecognition.R.drawable.ic_cancel)
            .setPositiveButton("Ok") { _, _ -> }
            .show()
    }

    fun setUser() {}
    fun progressBar_Image(Status: Boolean) {
        if (!Status) {
            signUpActivity.progressBar_Image.visibility = View.VISIBLE
        } else {
            signUpActivity.progressBar_Image.visibility = View.GONE
        }
    }


}