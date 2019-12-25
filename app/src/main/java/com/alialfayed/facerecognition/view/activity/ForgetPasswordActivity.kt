package com.alialfayed.facerecognition.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.alialfayed.facerecognition.R
import com.alialfayed.facerecognition.viewmodel.ForgetPasswordViewModel
import kotlinx.android.synthetic.main.activity_forget_password.*
import kotlinx.android.synthetic.main.activity_sign_in.*

class ForgetPasswordActivity : AppCompatActivity() {

    // References of Model class -> this for connection to server and activity

    private lateinit var forgetPasswordViewModel: ForgetPasswordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        this.forgetPasswordViewModel = ViewModelProviders.of(this,MyViewModelFactory(this)).get(ForgetPasswordViewModel::class.java)

        // reset Password
        btn_ForgetPassword.setOnClickListener {
            val email = edtEmail_ForgetPassword.text.toString()

            if (email.isNullOrEmpty()){
                edtEmail_SignIn.error = "Email Required!\nPlease, Type your Email!"
                edtEmail_SignIn.requestFocus()
            }else if (!forgetPasswordViewModel.isNetworkConnected()){
                AlertDialog.Builder(this@ForgetPasswordActivity)
                    .setTitle("Error")
                    .setMessage("Your Data transfer and Wifi connection closed!\nOpen Internet Connection and try again!")
                    .setIcon(R.drawable.ic_cancel)
                    .setPositiveButton("Ok") { _, _ ->  }
                    .show()
            }else{
                forgetPasswordViewModel.resetPassword(email)
            }
        }


    }

    // this is attached by Forget Password Activity and Forget Password ViewModel
    @Suppress("UNCHECKED_CAST")
    internal class MyViewModelFactory(private val forgetPasswordActivity: ForgetPasswordActivity) :ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ForgetPasswordViewModel(forgetPasswordActivity)as T
        }

    }
    // when user go back button finish Activity
    override fun onBackPressed() {
        finish()
    }
}
