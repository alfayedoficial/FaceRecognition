package com.alialfayed.facerecognition.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.alialfayed.facerecognition.R
import com.alialfayed.facerecognition.model.User
import com.alialfayed.facerecognition.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add_patient.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_sign_in.*

class ProfileActivity : AppCompatActivity() , View.OnClickListener{
    override fun onClick(p0: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var profileViewModel: ProfileViewModel
    lateinit var mdatabaseReference : DatabaseReference
    internal lateinit var userDatabase :User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        profileViewModel = ViewModelProviders.of(this,MyViewModeFactory(this)).get(ProfileViewModel::class.java)
        mdatabaseReference = FirebaseDatabase.getInstance().getReference("User")
        mdatabaseReference.keepSynced(true)
        userDatabase = User()
        onClickNav()

        btn_Edit_Profile.setOnClickListener {
            profileViewModel.display(true)
        }
        btn_Save_Profile.setOnClickListener {
            val oldPassword = edtPassword_Profile2.text.toString()
            val newPassword = edtPassword_Profile.text.toString()
            val confirmNewPassword = edtConfirmPassword_Profile.text.toString()
            if (!newPassword.equals(confirmNewPassword)){
                edtConfirmPassword_Profile.error = "Password not matched"
                edtConfirmPassword_Profile.requestFocus()
            }else if (oldPassword.isNullOrEmpty()){
                edtPassword_Profile2.error = "Fields must not be empty!"
                edtPassword_Profile2.requestFocus()
            }else if (newPassword.isNullOrEmpty()){
                edtPassword_Profile.error = "Fields must not be empty!"
                edtPassword_Profile.requestFocus()
            }else if (confirmNewPassword.isNullOrEmpty()){
                edtConfirmPassword_Profile.error = "Fields must not be empty!"
                edtConfirmPassword_Profile.requestFocus()

            }else if (newPassword.equals(oldPassword)){
                edtConfirmPassword_Profile.error = "Sorry you can not set the same password"
                edtConfirmPassword_Profile.requestFocus()

            }else {
                profileViewModel.changepassword(oldPassword ,newPassword )
            }
        }

    }


    fun update(){

    }

    override fun onStart() {
        super.onStart()
        mdatabaseReference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (profileSnapShot in p0.children){
                    val profileDatabase = profileSnapShot.getValue(User::class.java)
                    if (profileDatabase!!.getuserId() ==FirebaseAuth.getInstance().currentUser?.uid){
                        userDatabase = profileDatabase
                    }
                    profileViewModel.getData(userDatabase)
                }
            }

        })
    }

    @Suppress("UNCHECKED_CAST")
    internal class MyViewModeFactory(private val profileActivity: ProfileActivity):ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ProfileViewModel(profileActivity) as T
        }

    }

    private fun onClickNav(){


        /**
         * Actions
         */
        btnHome_Profile.setOnClickListener {
            val startActivity = Intent(this,HomeActivity::class.java)
            startActivity(startActivity)
        }
        btnPatient_Profile.setOnClickListener {
            val startActivity = Intent(this,AddPatientActivity::class.java)
            startActivity(startActivity)
        }
        btnAddNumber_Profile_Profile.setOnClickListener {
            val startActivity = Intent(this,AddNumberCallActivity::class.java)
            startActivity(startActivity)
        }
        btnProfile_Profile.setOnClickListener {
            val startActivity = Intent(this,ProfileActivity::class.java)
            startActivity(startActivity)
        }
        btnPolicy_Profile.setOnClickListener {
            val startActivity = Intent(this,PolicyActivity::class.java)
            startActivity(startActivity)
        }

    }
}
