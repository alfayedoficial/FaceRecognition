package com.alialfayed.facerecognition.viewmodel

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import com.alialfayed.facerecognition.R
import com.alialfayed.facerecognition.repository.FirebaseHandler
import com.alialfayed.facerecognition.view.activity.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*

/**
 * Class do :
 * Created by ( Eng Ali)
 */
class SignInViewModel(val signInActivity: SignInActivity) : ViewModel() {
    private var firebaseHandler: FirebaseHandler = FirebaseHandler(signInActivity, this)

    /**
     * method go to home Activity After check method on viewmodel
     */
    fun SignInSuccessful() {
        val email = signInActivity.edtEmail_SignIn.text.toString()
        val start = Intent(signInActivity, HomeActivity::class.java)
        start.putExtra("Email",email)
        signInActivity.startActivity(start)
        signInActivity.finish()

    }

    /**
     * method go to home Activity After check method on viewmodel
     */
    fun SignInfailed() {
        signInActivity.disableLayout(true)
    }

    /**
     * to complete data
     */
    fun completedata(){
        val start = Intent(signInActivity, InfoActivity::class.java)
        signInActivity.startActivity(start)
        signInActivity.finish()
    }

    fun setMsgAlert(msg: String) {
        AlertDialog.Builder(signInActivity)
            .setTitle("Error")
            .setMessage(msg)
            .setIcon(R.drawable.ic_cancel)
            .setPositiveButton("Ok") { _, _ -> }
            .show()
    }

    fun isNetworkConnected(): Boolean {
        val connectivityManager =
            signInActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }

    /**
     * method check if editText is not null or empty
     */
    fun signInCheck(email: String, password: String) {
        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            signInActivity.disableLayout(false)
            firebaseHandler?.signIn(email, password)
        } else {
            val msg = signInActivity.getString(R.string.message_empty_error)
            setMsgAlert(msg)
            signInActivity.disableLayout(true)
        }
    }


    /**
     * method go to SignUp Activity
     */
    fun goCreatAnAcount() {
        val intentAcount = Intent(signInActivity, SignUpActivity::class.java)
        signInActivity.startActivity(intentAcount)
    }

    /**
     * method go to ForgetPassword Activity
     */
    fun goForgetPassword() {
        val intentAcount = Intent(signInActivity, ForgetPasswordActivity::class.java)
        signInActivity.startActivity(intentAcount)
    }
}