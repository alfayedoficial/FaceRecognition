package com.alialfayed.facerecognition.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.alialfayed.facerecognition.R
import com.alialfayed.facerecognition.model.PhoneModel
import com.alialfayed.facerecognition.view.activity.HomeActivity.Companion.phoneListData
import com.alialfayed.facerecognition.viewmodel.AddNumberCallViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add_number_call.*

/**
 * Class do : this class of Add Number Call Activity
 * Created by ( Eng Ali)
 */

class AddNumberCallActivity : AppCompatActivity() {

    // References of Model class -> this for connection to server and activity

    private lateinit var addNumberCallViewModel: AddNumberCallViewModel
    private var mdatabaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Database")
    private var mPhoneDatabaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("PhoneList")


    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_number_call)
//        Reference of Model class
        addNumberCallViewModel = ViewModelProviders.of(
            this, MyViewModelFactory(this)
        ).get(AddNumberCallViewModel::class.java)
        // set Button on ready to able to click
        onClickNav()
        // Save Numbers
        btn_Save_Add_Number.setOnClickListener {
            addNumberCallViewModel.setNumber()
        }
        // Reset Numbers
        btn_Reset_Add_Number.setOnClickListener {
            AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Reset Numbers List")
                .setMessage("Do You Want To Reset Numbers List ?")
                .setIcon(R.drawable.ic_cancel)
                .setPositiveButton("Yes") { _, _ ->
                    delete()
                }
                .setNegativeButton("No") { _, _ -> }
                .show()

        }

    }

    private fun onClickNav() {
        /**
         * Set Button on ready to able to click
         */
        btnHome_Number_Add_Number_Call.setOnClickListener {
            val startActivity = Intent(this, HomeActivity::class.java)
            startActivity(startActivity)
        }
        btnPatient_Number_Add_Number_Call.setOnClickListener {
            val startActivity = Intent(this, AddPatientActivity::class.java)
            startActivity(startActivity)
        }
        btnAddNumber_Number_Add_Number_Call.setOnClickListener {
            val startActivity = Intent(this, AddNumberCallActivity::class.java)
            startActivity(startActivity)
        }
        btnProfile_Number_Add_Number_Call.setOnClickListener {
            val startActivity = Intent(this, ProfileActivity::class.java)
            startActivity(startActivity)
        }
        btnPolicy_Number_Add_Number_Call.setOnClickListener {
            val startActivity = Intent(this, PolicyActivity::class.java)
            startActivity(startActivity)
        }

    }

    // this is attached by Add Number Call Activity and Add Number Call ViewModel
    @Suppress("UNCHECKED_CAST")
    internal class MyViewModelFactory(private val addNumberCallActivity: AddNumberCallActivity) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AddNumberCallViewModel(addNumberCallActivity) as T
        }

    }

    // this is work when Activity started
    override fun onStart() {
        super.onStart()

        // this is work when Activity started
        mPhoneDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // get data from PhoneList Data
                for (phoneSnapShot in dataSnapshot.children) {
                    // get data from PhoneList Data kind of PhoneModel
                    val phSnapshot = phoneSnapShot.getValue(PhoneModel::class.java)
                    // check if user in data
                    if (phSnapshot!!.getuserId() == FirebaseAuth.getInstance().currentUser!!.uid) {
                        // check if user have data
                        if (phSnapshot!!.getvisibleData() == "true") {
                            // actions if true
                            scrollViewAddNumbber.visibility = View.GONE
                            textView9.visibility = View.VISIBLE
                            btn_Reset_Add_Number.visibility = View.VISIBLE
                            Log.i("PhoneActivity", "visibleData == true")

                        } else {
                            // actions if false
                            scrollViewAddNumbber.visibility = View.VISIBLE
                            textView9.visibility = View.GONE
                            btn_Reset_Add_Number.visibility = View.GONE
                            Log.i("PhoneActivity", "visibleData == false")

                        }
                    }else{

                    }
                }
            }
        })
    }

    // when user go back button finish Activity
    override fun onBackPressed() {
        finish()
    }
    // Reset Numbers
    fun delete() {
        mPhoneDatabaseReference.child(phoneListData.getPhoneID()).removeValue()

    }
}
