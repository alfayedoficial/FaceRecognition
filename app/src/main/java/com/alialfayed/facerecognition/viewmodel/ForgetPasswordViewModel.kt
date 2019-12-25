package com.alialfayed.facerecognition.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import com.alialfayed.facerecognition.repository.FirebaseHandler
import com.alialfayed.facerecognition.view.activity.ForgetPasswordActivity

/**
 * Class do :
 * Created by ( Eng Ali)
 */
class ForgetPasswordViewModel(val forgetPasswordActivity: ForgetPasswordActivity):ViewModel() {

    // References of Firebase class -> this for connection to server
    private  var firebaseHandler: FirebaseHandler =
        FirebaseHandler(forgetPasswordActivity,this)

    // check network
    fun isNetworkConnected(): Boolean {
        val connectivityManager =
            forgetPasswordActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }

    // Reset Password
    fun resetPassword(email :String){
        firebaseHandler.resetPassword(email)
    }
}