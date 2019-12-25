package com.alialfayed.facerecognition.model

/**
 * Class do : this class of patient Information
 * Created by ( Eng Ali)
 */
class PatientModel {
    private lateinit var userId :String
    private lateinit var patientId :String
    private lateinit var patientCode:String
    private lateinit var patientName :String
    private lateinit var patientAge :String
    private lateinit var patientStatus :String
    private lateinit var patientimage1 :String
    private lateinit var patientimage2 :String
    private lateinit var patientimage3 :String
    private lateinit var patientimage4 :String
    private lateinit var patientimage5 :String
    private lateinit var visibleData :String

    constructor()

    constructor(mUserId :String, mPatientId :String ,mPatientCode:String,mPatientName:String ,
                mPatientAge :String ,mPatientStatus :String,
                mPatientImage_1:String , mPatientImage_2 :String ,
                mPatientImage_3 :String , mPatientImage_4 :String,
                mPatientImage_5 :String , mVisibleData :String){
        this.userId = mUserId
        this.patientId =mPatientId
        this.patientCode = mPatientCode
        this.patientName = mPatientName
        this.patientAge = mPatientAge
        this.patientStatus =mPatientStatus
        this.patientimage1 =mPatientImage_1
        this.patientimage2 =mPatientImage_2
        this.patientimage3 =mPatientImage_3
        this.patientimage4 =mPatientImage_4
        this.patientimage5 =mPatientImage_5
        this.visibleData =mVisibleData

    }
    constructor(mUserId :String, mPatientCode :String ,mPatientImage_1 :String){
        this.userId = mUserId
        this.patientCode = mPatientCode
        this.patientimage1 = mPatientImage_1
    }
    fun getuserId ():String{ return userId}
    fun getpatientId ():String{ return patientId}
    fun getpatientCode ():String{ return patientCode}
    fun getpatientName ():String{ return patientName}
    fun getpatientAge ():String{ return patientAge}
    fun getpatientStatus ():String{ return patientStatus}
    fun getpatientImage_1 ():String{ return patientimage1}
    fun getpatientImage_2 ():String{ return patientimage2}
    fun getpatientImage_3 ():String{ return patientimage3}
    fun getpatientImage_4 ():String{ return patientimage4}
    fun getpatientImage_5 ():String{ return patientimage5}
    fun getvisibleData ():String{ return visibleData}



}