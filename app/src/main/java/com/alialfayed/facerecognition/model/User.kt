package com.alialfayed.facerecognition.model

/**
 * Class do :
 * Created by ( Eng Ali)
 */
class User {
    private lateinit var userId :String
    private lateinit var userName :String
    private lateinit var userAge :String
    private lateinit var userPhone :String
    private lateinit var userImage :String

    constructor()

    constructor( userId :String ,userName:String  , userAge:String , userPhone :String , userImage :String){
        this.userId = userId
        this.userName = userName
        this.userAge = userAge
        this.userPhone = userPhone
        this.userImage = userImage
    }

    fun getuserId():String{ return userId}
    fun getUserName():String { return userName}
    fun getUserAge():String{return  userAge}
    fun getUSerPhone():String{ return userPhone}
    fun getUSerImage():String{ return userImage}





}