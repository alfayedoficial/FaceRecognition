package com.alialfayed.facerecognition.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import com.alialfayed.facerecognition.model.User
import com.alialfayed.facerecognition.repository.FirebaseHandler
import com.alialfayed.facerecognition.view.activity.ProfileActivity
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileViewModel(private val profileActivity: ProfileActivity) : ViewModel() {

    private var mfirebaseHandler: FirebaseHandler = FirebaseHandler(this.profileActivity,this)

    fun getData(userDatabase : User){
        this.profileActivity.edtName_Profile.setText(userDatabase.getUserName())
        this.profileActivity.edtId_Profile.setText(this.mfirebaseHandler.getUserEmail().toString())
        this.profileActivity.edtAge_Profile.setText(userDatabase.getUserAge())
        this.profileActivity.edtPhone_Profile.setText(userDatabase.getUSerPhone())
    }

    fun display( status : Boolean) = if (status){
        this.profileActivity.edtName_Profile.visibility = View.GONE
        this.profileActivity.textView4.visibility = View.GONE
        this.profileActivity.edtId_Profile.visibility = View.GONE
        this.profileActivity.textView5.visibility = View.GONE
        this.profileActivity.edtAge_Profile.visibility = View.GONE
        this.profileActivity.textView7.visibility = View.GONE
        this.profileActivity.edtPhone_Profile.visibility = View.GONE
        this.profileActivity.textView6.visibility = View.GONE
        this.profileActivity.btn_Edit_Profile.visibility = View.GONE
        this.profileActivity.edtConfirmPassword_Profile.visibility = View.VISIBLE
        this.profileActivity.edtPassword_Profile.visibility = View.VISIBLE
        this.profileActivity.edtPassword_Profile2.visibility = View.VISIBLE
        this.profileActivity.btn_Save_Profile.visibility = View.VISIBLE
    }else{
        this.profileActivity.edtName_Profile.visibility = View.VISIBLE
        this.profileActivity.textView4.visibility = View.VISIBLE
        this.profileActivity.edtId_Profile.visibility = View.VISIBLE
        this.profileActivity.textView5.visibility = View.VISIBLE
        this.profileActivity.edtAge_Profile.visibility = View.VISIBLE
        this.profileActivity.textView7.visibility = View.VISIBLE
        this.profileActivity.edtPhone_Profile.visibility = View.VISIBLE
        this.profileActivity.textView6.visibility = View.VISIBLE
        this.profileActivity.btn_Edit_Profile.visibility = View.VISIBLE
        this.profileActivity.edtConfirmPassword_Profile.visibility = View.GONE
        this.profileActivity.edtPassword_Profile.visibility = View.GONE
        this.profileActivity.edtPassword_Profile2.visibility = View.GONE
        this.profileActivity.btn_Save_Profile.visibility = View.GONE
    }

    fun changepassword(oldPass:String , newPass:String){
        this.mfirebaseHandler.changePassword(oldPass ,newPass )
    }



}