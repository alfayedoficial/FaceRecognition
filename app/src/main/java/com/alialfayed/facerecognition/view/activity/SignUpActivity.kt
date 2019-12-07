package com.alialfayed.facerecognition.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.alialfayed.facerecognition.viewmodel.SignUpViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import android.widget.Toast
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.alialfayed.facerecognition.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*


class SignUpActivity : AppCompatActivity() , View.OnClickListener{

    private lateinit var signUpViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.alialfayed.facerecognition.R.layout.activity_sign_up)
        this.signUpViewModel = ViewModelProviders.of(this,MyViewModelFactory(this)).get(SignUpViewModel::class.java)
        initComponent()

    }

    private fun initComponent() {
        btnSignUp_SignUp.setOnClickListener(this)
        btnSignIn_SignUp.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        val email = edtEmail_SignUp.text.toString().trim()
        val password = edtPassword_SignUp.text.toString().trim()
        val confirmPassword = edtConfirmPassword_Signup.text.toString().trim()

        when (view?.id) {
            R.id.btnSignUp_SignUp -> {

                if (email.isEmpty()){
                    edtEmail_SignUp.error = "Email Required!\nPlease, Type your Email!"
                    edtEmail_SignUp.requestFocus()

                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    edtEmail_SignUp.error = "Valid Email Required!\nlike: example@example.com"
                    edtEmail_SignUp.requestFocus()

                } else if (password.isEmpty()){
                    edtPassword_SignUp.error = "Password Required!\nPlease, Type your Password!"
                    edtPassword_SignUp.requestFocus()

                } else if (password.length < 6){
                    edtPassword_SignUp.error = "Password should be at least 6 characters long!"
                    edtPassword_SignUp.requestFocus()

                } else if (confirmPassword.isEmpty()){
                    edtConfirmPassword_Signup.error = "Confirm Password Required!\nPlease, Confirm your Password!"
                    edtConfirmPassword_Signup.requestFocus()

                } else if (password != confirmPassword){
                    AlertDialog.Builder(this@SignUpActivity)
                        .setTitle("Error")
                        .setMessage("Two passwords incompatible!")
                        .setIcon(com.alialfayed.facerecognition.R.drawable.ic_cancel)
                        .setPositiveButton("Ok") { _, _ ->  }
                        .show()

                } else if (!signUpViewModel.isNetworkConnected()){
                    AlertDialog.Builder(this@SignUpActivity)
                        .setTitle("Error")
                        .setMessage("Your Data transfer and Wifi connection closed!\nOpen Internet Connection and try again!")
                        .setIcon(com.alialfayed.facerecognition.R.drawable.ic_cancel)
                        .setPositiveButton("Ok") { _, _ ->  }
                        .show()
                } else {
                    signUpViewModel.signUpCheck(email, password)
                }
            }
            R.id.btnSignIn_SignUp ->{
                val startIntent = Intent(this , SignInActivity::class.java)
                startActivity(startIntent)
            }
        }
    }



    fun disableLayout(status: Boolean) {
        edtEmail_SignUp.isEnabled = status
        edtPassword_SignUp.isEnabled = status
        edtConfirmPassword_Signup.isEnabled = status
        btnSignUp_SignUp.isEnabled = status
        btnSignIn_SignUp.isEnabled = status

    }


    override fun onBackPressed() {
        finish()
    }

    @Suppress("UNCHECKED_CAST")
    internal class MyViewModelFactory(private val signUpActivity: SignUpActivity):
        ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SignUpViewModel(signUpActivity) as T
        }

    }


}
