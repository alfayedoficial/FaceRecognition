package com.alialfayed.facerecognition.view.activity

import android.annotation.SuppressLint
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
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_profile.*

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
        // set Button on ready to able to click
        onClickNav()

        // edit profile
        btn_Edit_Profile.setOnClickListener {
            profileViewModel.display(true)
        }
        // save profile
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

    // this is work when Activity started
    override fun onStart() {
        super.onStart()
        // this is work when Activity started
        mdatabaseReference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // get data from User Data
                for (profileSnapShot in dataSnapshot.children){
                    if (profileSnapShot.hasChildren()){
                        // get data from profileSnapShot Data kind of User
                        val profileDatabase = profileSnapShot.getValue(User::class.java)
                        // check if user in data
                        if (profileDatabase!!.getuserId() == FirebaseAuth.getInstance().currentUser!!.uid){
                            // check if user have data
                            if (profileDatabase.getvisibleData() == "true"){
                                // actions if true
                                userDatabase = profileDatabase
                                profileViewModel.getData(userDatabase)
                            }
                        }
                    }
                    // no have childern all data
                }
            }
        })
    }

    // this is attached by Profile Activity and Profile ViewModel
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
            finish()

        }
        btnPatient_Profile.setOnClickListener {
            val startActivity = Intent(this,AddPatientActivity::class.java)
            startActivity(startActivity)
            finish()

        }
        btnAddNumber_Profile_Profile.setOnClickListener {
            val startActivity = Intent(this,AddNumberCallActivity::class.java)
            startActivity(startActivity)
            finish()

        }
        btnProfile_Profile.setOnClickListener {
            val startActivity = Intent(this,ProfileActivity::class.java)
            startActivity(startActivity)
            finish()

        }
        btnPolicy_Profile.setOnClickListener {
            val startActivity = Intent(this,PolicyActivity::class.java)
            startActivity(startActivity)
            finish()

        }

    }

    // when user go back button finish Activity
    override fun onBackPressed() {
        finish()
    }
    // set image to profile
    fun getImage(){
        Glide.with(this)
            .load(userDatabase.getUSerImage())
            .into(ProfileImage)
    }
}
