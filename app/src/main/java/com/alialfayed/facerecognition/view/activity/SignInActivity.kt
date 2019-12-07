package com.alialfayed.facerecognition.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.alialfayed.facerecognition.R
import com.alialfayed.facerecognition.viewmodel.SignInViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.security.AccessController.getContext

class SignInActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var signInViewModel : SignInViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        signInViewModel = ViewModelProviders.of(this,MyViewModelFactory(this)).get(SignInViewModel::class.java)
        initComponent()

    }

    /**
     * method check by Ids
     */
    override fun onClick(view: View?) {
        var email = edtEmail_SignIn.text.toString().trim()
        var password = edtPassword_SignIn.text.toString().trim()

        when (view?.id) {
            R.id.btnSignIn_SignIn -> {

                if (email.isEmpty()){
                    edtEmail_SignIn.error = "Email Required!\nPlease, Type your Email!"
                    edtEmail_SignIn.requestFocus()

                } else if (password.isEmpty()){
                    edtPassword_SignIn.error = "Password Required!\nPlease, Type your Password!"
                    edtPassword_SignIn.requestFocus()

                } else if (!signInViewModel.isNetworkConnected()){
                    AlertDialog.Builder(this@SignInActivity)
                        .setTitle("Error")
                        .setMessage("Your Data transfer and Wifi connection closed!\nOpen Internet Connection and try again!")
                        .setIcon(R.drawable.ic_cancel)
                        .setPositiveButton("Ok") { _, _ ->  }
                        .show()

                } else {
                    signInViewModel.signInCheck(email, password)
                }

            }
            R.id.btnGoogle_SignIn -> {
//                signIn()
            }
            R.id.btnForgetPassword_SignIn -> {
                signInViewModel.goForgetPassword()
            }
            R.id.btnCreateAnAccount_SignIn -> {
                signInViewModel.goCreatAnAcount()
            }
        }
    }

    private fun initComponent() {
        btnSignIn_SignIn.setOnClickListener(this)
        btnGoogle_SignIn.setOnClickListener(this)
        btnForgetPassword_SignIn.setOnClickListener(this)
        btnCreateAnAccount_SignIn.setOnClickListener(this)
    }

    fun disableLayout(status: Boolean) {
        edtEmail_SignIn.isEnabled = status
        edtPassword_SignIn.isEnabled = status
        btnSignIn_SignIn.isEnabled = status
        btnGoogle_SignIn.isEnabled = status
        btnForgetPassword_SignIn.isEnabled = status
        btnCreateAnAccount_SignIn.isEnabled = status
        if (!status) {
            progressBar_SignIn.visibility = View.VISIBLE
        } else {
            progressBar_SignIn.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        finish()
    }

    internal class MyViewModelFactory(val signInActivity: SignInActivity):
        ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SignInViewModel(signInActivity) as T
        }

    }
}
