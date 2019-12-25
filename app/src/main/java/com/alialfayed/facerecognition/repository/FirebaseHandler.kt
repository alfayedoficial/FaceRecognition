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
 * Class do : this class of Firebase Server
 * here all action of data insert of get from Firebase do here
 * Created by ( Eng Ali)
 */
class FirebaseHandler(activity: Activity) {

    // references of all classes connected to server
    // All references private to protect data

    private var mAuth: FirebaseAuth
    private var currentUser: FirebaseUser? = null
    private var databaseReference: DatabaseReference
    private var databaseUser: DatabaseReference
    private var databasePhone: DatabaseReference
    private var signInViewModel: SignInViewModel? = null
    private var signUpViewModel: SignUpViewModel? = null
    private var homeViewModel: HomeActivityViewModel? = null
    private var infoViewModel: InfoViewModel? = null
    private var forgetPasswordViewModel: ForgetPasswordViewModel? = null
    private var profileViewModel: ProfileViewModel? = null
    private var addPatientViewModel: AddPatientViewModel? = null
    private var addNumberCallViewModel: AddNumberCallViewModel? = null


    // All constructor of classes
    var activity: Activity? = null

    init {
        this.activity = activity
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
        databaseReference = FirebaseDatabase.getInstance().getReference("Database")
        databaseUser = FirebaseDatabase.getInstance().getReference("User")
        databasePhone = FirebaseDatabase.getInstance().getReference("PhoneList")
    }

    constructor(activity: Activity, signInViewModel: SignInViewModel) : this(activity) {
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

    constructor(activity: Activity, infoViewModel: InfoViewModel) : this(activity) {
        this.infoViewModel = infoViewModel
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
    }

    constructor(activity: Activity, forgetPasswordViewModel: ForgetPasswordViewModel) : this(
        activity
    ) {
        this.forgetPasswordViewModel = forgetPasswordViewModel
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
    }

    constructor(activity: Activity, profileViewModel: ProfileViewModel) : this(activity) {
        this.profileViewModel = profileViewModel
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
    }

    constructor(activity: Activity, addPatientViewModel: AddPatientViewModel) : this(activity) {
        this.addPatientViewModel = addPatientViewModel
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
    }

    constructor(
        activity: Activity,
        addNumberCallViewModel: AddNumberCallViewModel
    ) : this(activity) {
        this.addNumberCallViewModel = addNumberCallViewModel
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser

    }


    // get user
    private fun getFirebaseUser(): FirebaseUser {
        return mAuth.currentUser!!
    }

    // sign In
    fun signIn(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                if (mAuth.currentUser!!.isEmailVerified) {
                    Log.i("SigninFirebase", "1")
                    databaseUser.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            Log.i("SigninFirebase", "2")
                            // get data from User Data
                            for (profileSnapShot in dataSnapshot.children) {
                                Log.i("SigninFirebase", "3")
                                // get data from profileSnapShot Data kind of User
                                val profileDatabase = profileSnapShot.getValue(User::class.java)
                                Log.i("SigninFirebase", "4")
                                // check if user in data
                                if (profileDatabase!!.getuserId() == FirebaseAuth.getInstance().currentUser!!.uid) {
                                    Log.i("SigninFirebase", "5")
                                    // check if user have data
                                    if (profileDatabase.getvisibleData() == "true") {
                                        Log.i("SigninFirebase", "6")
                                        // actions if true
                                        Toast.makeText(
                                            activity,
                                            " Success $email",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        signInViewModel?.SignInSuccessful()
                                    } else {
                                        Log.i("SigninFirebase", "7")
                                        signInViewModel?.goToInfo()
                                    }
                                    Log.i("SigninFirebase", "8")
                                } else {
                                    signInViewModel?.goToInfo()
                                }
                                Log.i("SigninFirebase", "9")
                                // no have childern all data
                            }
                            Log.i("SigninFirebase", "10")
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

    // sign Up
    fun signUp(email: String, password: String) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.i("Signup", "13")
                mAuth.currentUser!!.sendEmailVerification()
                Log.i("Signup", "14")
                signUpViewModel?.SignUpSuccessful()
            } else {
                signUpViewModel?.SignUpfailed()
            }
        }
    }

    // Reset Password
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

    // Log Out
    fun logout() {
        mAuth.signOut()
    }

    // Insert UserData
    fun setUser(userName: String, userAge: String, userPhone: String, userImage: String) {
        // Push new id of User
        val useId = databaseUser.push().key.toString()
        val user = mAuth.currentUser!!.uid
        // Create class of new User information
        val userDate = User(user, userName, userAge, userPhone, userImage, "true")
        // insert class in database
        databaseUser.child(useId).setValue(userDate)
        // check id data save or now // this result run on Logcat android studio for developer
        Log.i("TAG", "Save data")
    }

    // Insert PatientData
    fun setPatient(
        patientId: String, patientName: String, patientAge: String, patientStatus: String,
        patientImage1: String,
        patientImage2: String,
        patientImage3: String,
        patientImage4: String,
        patientImage5: String
    ) {
        // Push new id of patient
        val patId = databaseReference.push().key.toString()
        val user = mAuth.currentUser!!.uid
        // Create class of new Patient information
        val patientDate = PatientModel(
            user,
            patientId,
            patId,
            patientName,
            patientAge,
            patientStatus,
            patientImage1,
            patientImage2,
            patientImage3,
            patientImage4,
            patientImage5,
            "true"
        )
        // insert class in database
        databaseReference.child(patId).setValue(patientDate)
        // go to add Number Activity
        addPatientViewModel?.setNumber()
    }

    // Insert PhoneData
    fun setPhone(
        patientNumberHome: String, patientNumberPolice: String, patientNumberDoctor: String,
        patientNumberHospital: String
    ) {
        // Push new id of phone
        val phoneID = databasePhone.push().key.toString()
        val user = mAuth.currentUser!!.uid
        // Create class of new phone information
        val phoneDate = PhoneModel(
            phoneID, user,
            patientNumberHome, patientNumberPolice,
            patientNumberDoctor, patientNumberHospital, "true"
        )
        // insert class in database
        databasePhone.child(phoneID).setValue(phoneDate)
    }

    // Change Password
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
                            Toast.makeText(
                                activity, " Password update ",
                                Toast.LENGTH_LONG
                            ).show()
                            profileViewModel?.display(false)

                        } else {
                            Toast.makeText(
                                activity, " Sorry your have Error , try again later",
                                Toast.LENGTH_LONG
                            ).show()
                            profileViewModel?.display(true)
                        }
                    }
                } else {
                    Toast.makeText(
                        activity, " Sorry your have Error , try again later ",
                        Toast.LENGTH_LONG
                    ).show()
                    profileViewModel?.display(true)
                }
            }
    }

    // get Email of user register
    fun getUserEmail(): String {
        val user = FirebaseAuth.getInstance().currentUser
        return user?.email.toString()
    }

    // set Status safety
    fun setSafety(patientId: String) {
        val user = mAuth.currentUser!!.uid
        val patientDate = PatientModel(user, patientId, "true")

//        databaseReference.child(patientId).setValue(patientDate)
        databaseReference.child(patientId).child("patientStatus").setValue("Safety")
    }

}