package com.alialfayed.facerecognition.model

/**
 * Class do : this class of user Information
 * Created by ( Eng Ali)
 */
class User {
    private lateinit var userId :String
    private lateinit var userName :String
    private lateinit var userAge :String
    private lateinit var userPhone :String
    private lateinit var userImage :String
    private lateinit var visibleData :String

    constructor()

    constructor( userId :String ,userName:String  , userAge:String , userPhone :String , userImage :String , mVisibleData :String){
        this.userId = userId
        this.userName = userName
        this.userAge = userAge
        this.userPhone = userPhone
        this.userImage = userImage
        this.visibleData =mVisibleData
    }

    fun getuserId():String{ return userId}
    fun getUserName():String { return userName}
    fun getUserAge():String{return  userAge}
    fun getUSerPhone():String{ return userPhone}
    fun getUSerImage():String{ return userImage}
    fun getvisibleData():String{ return visibleData}





}