package com.alialfayed.facerecognition.model

/**
 * Class do : this class of Phone Information
 * Created by ( Eng Ali)
 */
class PhoneModel {
    private lateinit var phoneID : String
    private lateinit var userId : String
    private lateinit var patientNumberHome :String
    private lateinit var patientNumberPolice :String
    private lateinit var patientNumberDoctor :String
    private lateinit var patientNumberHospital :String
    private lateinit var visibleData :String

    constructor()

    constructor(phoneID : String  , UserID : String,mPatientNumberHome :String , mPatientNumberPolice :String ,
                mPatientNumberDoctor :String , mPatientNumberHospital :String , mVisibleData :String){
        this.phoneID = phoneID
        this.userId = UserID
        this.patientNumberHome =mPatientNumberHome
        this.patientNumberPolice = mPatientNumberPolice
        this.patientNumberDoctor =mPatientNumberDoctor
        this.patientNumberHospital =mPatientNumberHospital
        this.visibleData =mVisibleData
    }

    fun getPhoneID() :String {return  phoneID}
    fun getuserId():String{return  userId}
    fun getpatientNumberHome ():String{ return patientNumberHome}
    fun getpatientNumberPolice ():String{ return patientNumberPolice}
    fun getpatientNumberDoctor ():String{ return patientNumberDoctor}
    fun getpatientNumberHospital ():String{ return patientNumberHospital}
    fun getvisibleData ():String{ return visibleData}

}