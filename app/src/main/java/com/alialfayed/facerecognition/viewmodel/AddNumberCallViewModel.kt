package com.alialfayed.facerecognition.viewmodel

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.alialfayed.facerecognition.repository.FirebaseHandler
import com.alialfayed.facerecognition.view.activity.AddNumberCallActivity
import com.alialfayed.facerecognition.view.activity.HomeActivity
import kotlinx.android.synthetic.main.activity_add_number_call.*

class AddNumberCallViewModel(private val addNumberCallActivity: AddNumberCallActivity) : ViewModel() {

    private  var firebaseHandler: FirebaseHandler = FirebaseHandler(addNumberCallActivity , this)

    fun setNumber(){
        val home = addNumberCallActivity.edtHomeNumber_Add_Number_Call.text.toString()
        val police = addNumberCallActivity.edtPolice_Number_Add_Number_Call.text.toString()
        val doctor = addNumberCallActivity.edtDoctor_Number_Add_Number_Call.text.toString()
        val hospital = addNumberCallActivity.edtHospital_Number_Add_Number_Call.text.toString()

        when {
            home.isEmpty() -> {
                addNumberCallActivity.edtHomeNumber_Add_Number_Call.error =
                    "Fields must not be empty!"
                addNumberCallActivity.edtHomeNumber_Add_Number_Call.requestFocus()

            }
            police.isEmpty() -> {
                addNumberCallActivity.edtPolice_Number_Add_Number_Call.error =
                    "Fields must not be empty!"
                addNumberCallActivity.edtPolice_Number_Add_Number_Call.requestFocus()

            }
            doctor.isEmpty() -> {
                addNumberCallActivity.edtDoctor_Number_Add_Number_Call.error =
                    "Fields must not be empty!"
                addNumberCallActivity.edtDoctor_Number_Add_Number_Call.requestFocus()

            }
            hospital.isEmpty() -> {
                addNumberCallActivity.edtHospital_Number_Add_Number_Call.error =
                    "Fields must not be empty!"
                addNumberCallActivity.edtHospital_Number_Add_Number_Call.requestFocus()

            }
            else ->{
             firebaseHandler.setPhone(home, police, doctor, hospital)
            val start = Intent(addNumberCallActivity,HomeActivity::class.java)
                addNumberCallActivity.startActivity(start)

            }

        }

    }

}