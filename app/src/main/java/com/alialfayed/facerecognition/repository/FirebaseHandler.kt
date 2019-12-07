package com.alialfayed.facerecognition.repository

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.alialfayed.facerecognition.model.PatientModel
import com.alialfayed.facerecognition.model.PhoneModel
import com.alialfayed.facerecognition.model.User
import com.alialfayed.facerecognition.viewmodel.*
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

/**
 * Class do :
 * Created by ( Eng Ali)
 */
class FirebaseHandler(activity: Activity) {
    private var mAuth: FirebaseAuth
    private var currentUser: FirebaseUser? = null
    private  var databaseReference: DatabaseReference
    private  var databaseUser: DatabaseReference
    private  var databasePhone: DatabaseReference
    private var signInViewModel: SignInViewModel? = null
    private var signUpViewModel: SignUpViewModel? = null
    private var homeViewModel: HomeActivityViewModel? = null
    private var infoViewModel:InfoViewModel? = null
    private var forgetPasswordViewModel:ForgetPasswordViewModel? = null
    private var profileViewModel: ProfileViewModel? = null
    private var addPatientViewModel:AddPatientViewModel? = null
    private var patientData :PatientModel? = null
    private var addNumberCallViewModel:AddNumberCallViewModel? = null





    var activity: Activity? = null

    init {
        this.activity = activity
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
        databaseReference = FirebaseDatabase.getInstance().getReference("Database")
        databaseUser = FirebaseDatabase.getInstance().getReference("User")
        databasePhone = FirebaseDatabase.getInstance().getReference("PhoneList")
    }

    constructor(activity: Activity, signInViewModel: SignInViewModel): this(activity) {
        this.signInViewModel = signInViewModel
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
    }
    constructor(activity: Activity, signUpViewModel: SignUpViewModel)
            : this(activity) {
        this.signUpViewModel = signUpViewModel
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
    }

    constructor(activity: Activity, homeViewModel: HomeActivityViewModel)
            : this(activity) {
        this.homeViewModel = homeViewModel
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
    }

    constructor(activity: Activity,infoViewModel : InfoViewModel):this(activity){
        this.infoViewModel = infoViewModel
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
    }

    constructor(activity: Activity,forgetPasswordViewModel: ForgetPasswordViewModel) : this(activity){
        this.forgetPasswordViewModel = forgetPasswordViewModel
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
    }
    constructor(activity: Activity,profileViewModel: ProfileViewModel):this(activity){
        this.profileViewModel = profileViewModel
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
    }

    constructor(activity: Activity , addPatientViewModel: AddPatientViewModel):this(activity){
        this.addPatientViewModel = addPatientViewModel
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
    }

    constructor(activity: Activity , addNumberCallViewModel: AddNumberCallViewModel):this(activity){
        this.addNumberCallViewModel = addNumberCallViewModel
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser

    }

    private fun getFirebaseUser(): FirebaseUser {
        return mAuth.currentUser!!
    }

    fun signIn(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                if (mAuth.currentUser!!.isEmailVerified) {
                    Toast.makeText(activity, " Success $email", Toast.LENGTH_LONG).show()

                    databaseUser.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            for (profileSnapShot in p0.children){
                                val profileDatabase = profileSnapShot.getValue(User::class.java)
                                if (profileDatabase!!.getuserId() ==FirebaseAuth.getInstance().currentUser?.uid){
                                    signInViewModel?.SignInSuccessful()
                                }else{
                                    signInViewModel?.completedata()
                                }

                            }
                        }

                    })

                } else {
                    signInViewModel?.setMsgAlert("Please, verify your email address to login.")
                    signInViewModel?.SignInfailed()
                }

            } else {
                signInViewModel?.setMsgAlert("Sorry, $email Failed to sign in , please try again later")
                signInViewModel?.SignInfailed()
            }
        }
    }
    fun signUp(email: String, password: String) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                mAuth.currentUser!!.sendEmailVerification()
                signUpViewModel?.SignUpSuccessful()
            } else {
                signUpViewModel?.SignUpfailed()
            }
        }
    }
    fun resetPassword(email: String) {
        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.i("forgetPassword", "Success$email")
                    Toast.makeText(activity, "Please Check your Email", Toast.LENGTH_LONG).show()
        //                    forgetPasswodViewModel?.ForgetPasswordSuccessful()

                } else {
                    Log.i("forgetPassword", "Fail")
        //                    forgetPasswodViewModel?.ForgetPasswordfailed()
                }
            }
    }
    fun logout() {
        mAuth.signOut()
    }


    fun setUser(userName :String , userAge :String , userPhone :String , userImage :String){
        val useId = databaseUser.push().key.toString()
        val user = mAuth.currentUser!!.uid
        val userDate = User(user,userName,userAge,userPhone,userImage)
        Log.i("TAG","Save data")
        databaseUser.child(useId).setValue(userDate)

    }

    fun setPatient(patientId :String ,patientName :String , patientAge :String ,patientStatus:String,
                   patientImage1 :String,
                   patientImage2 :String,
                   patientImage3 :String,
                   patientImage4 :String,
                   patientImage5 :String ){
        val patId = databaseReference.push().key.toString()
        val user = mAuth.currentUser!!.uid
        val patientDate = PatientModel(user,patientId,patId,patientName,patientAge,patientStatus,patientImage1,patientImage2,
            patientImage3,patientImage4,patientImage5,"true")

        databaseReference.child(patId).setValue(patientDate)
        addPatientViewModel?.setNumber()
    }

    fun setPhone(patientNumberHome :String , patientNumberPolice :String , patientNumberDoctor :String ,
                    patientNumberHospital :String){

        val phoneID = databasePhone.push().key.toString()
        val user = mAuth.currentUser!!.uid
        val phoneDate = PhoneModel( phoneID, user,
            patientNumberHome,patientNumberPolice,
            patientNumberDoctor,patientNumberHospital )

        databasePhone.child(phoneID).setValue(phoneDate)
    }

    @Suppress("NAME_SHADOWING")
    fun changePassword(oldPassword: String, password: String) {
        val user = getFirebaseUser()
        val credential = EmailAuthProvider
            .getCredential(user.email!!, oldPassword)
        user.reauthenticate(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user.updatePassword(password).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(activity, " Password update ",
                                    Toast.LENGTH_LONG).show()
                                profileViewModel?.display(false)

                            } else {
                                Toast.makeText(activity, " Sorry your have Error , try again later",
                                    Toast.LENGTH_LONG ).show()
                                profileViewModel?.display(true)
                            }
                        }
                } else {
                    Toast.makeText(activity, " Sorry your have Error , try again later ",
                        Toast.LENGTH_LONG ).show()
                    profileViewModel?.display(true)
                }
            }
    }


    fun getUserEmail():String{
        val user = FirebaseAuth.getInstance().currentUser
        return user?.email.toString()
    }

}