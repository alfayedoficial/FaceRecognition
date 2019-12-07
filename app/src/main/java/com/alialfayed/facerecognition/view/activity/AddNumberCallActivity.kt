package com.alialfayed.facerecognition.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.alialfayed.facerecognition.R
import com.alialfayed.facerecognition.viewmodel.AddNumberCallViewModel
import com.alialfayed.facerecognition.viewmodel.InfoViewModel
import kotlinx.android.synthetic.main.activity_add_number_call.*
import kotlinx.android.synthetic.main.activity_add_patient.*

class AddNumberCallActivity : AppCompatActivity() {

    private lateinit var addNumberCallViewModel: AddNumberCallViewModel
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_number_call)

        addNumberCallViewModel = ViewModelProviders.of(this,MyViewModelFactory(this)
        ).get(AddNumberCallViewModel::class.java)

        onClickNav()


        btn_Save_Add_Number.setOnClickListener {
            addNumberCallViewModel.setNumber()
        }

    }

    private fun onClickNav(){
        /**
         * Actions
         */
        btnHome_Number_Add_Number_Call.setOnClickListener {
            val startActivity = Intent(this,HomeActivity::class.java)
            startActivity(startActivity)
        }
        btnPatient_Number_Add_Number_Call.setOnClickListener {
            val startActivity = Intent(this,AddPatientActivity::class.java)
            startActivity(startActivity)
        }
        btnAddNumber_Number_Add_Number_Call.setOnClickListener {
            val startActivity = Intent(this,AddNumberCallActivity::class.java)
            startActivity(startActivity)
        }
        btnProfile_Number_Add_Number_Call.setOnClickListener {
            val startActivity = Intent(this,ProfileActivity::class.java)
            startActivity(startActivity)
        }
        btnPolicy_Number_Add_Number_Call.setOnClickListener {
            val startActivity = Intent(this,PolicyActivity::class.java)
            startActivity(startActivity)
        }

    }

    @Suppress("UNCHECKED_CAST")
    internal class MyViewModelFactory(private val addNumberCallActivity: AddNumberCallActivity) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AddNumberCallViewModel(addNumberCallActivity) as T
        }

    }
}
