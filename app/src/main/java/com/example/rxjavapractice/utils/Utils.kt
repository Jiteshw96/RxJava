package com.example.rxjavapractice.utils

import android.hardware.usb.UsbRequest
import android.util.Log
import com.example.rxjavapractice.model.ApiUser
import com.example.rxjavapractice.model.User

object Utils {

    fun getUserList(): List<User> {
        val userList = ArrayList<User>()
        val userOne = User(firstName = "amit", lastName = "Shikhar")
        userList.add(userOne)

        val userTwo = User(firstName = "manish", lastName = "kumar")
        userList.add(userTwo)

        val userThree = User(firstName = "sumit", lastName = "Kumar")
        userList.add(userThree)

        return userList
    }

    fun getApiUserList(): List<ApiUser> {
        val userList = ArrayList<ApiUser>()
        val userOne = ApiUser(firstName = "amit", lastName = "Shikhar")
        userList.add(userOne)

        val userTwo = ApiUser(firstName = "manish", lastName = "kumar")
        userList.add(userTwo)

        val userThree = ApiUser(firstName = "sumit", lastName = "Kumar")
        userList.add(userThree)

        return userList
    }

    fun getUserWhoLovesCricket(): List<User> {
        val userList = ArrayList<User>()
        val userOne = User(id = 1, firstName = "Amit", lastName = "Shehkar")
        userList.add(userOne)

        val userTwo = User(id = 2, firstName = "manish", lastName = "kumar")
        userList.add(userTwo)
        return userList
    }


    fun getUserWhoLovesFootball(): List<User> {
        val userList = ArrayList<User>()

        val userOne = User(id = 1, firstName = "Amit", lastName = "Shehkar")
        userList.add(userOne)

        val userTwo = User(id = 3, firstName = "Sumit", lastName = "Kumar")
        userList.add(userTwo)

        return userList
    }

    fun convertApiUserListToUserList(apiUserList: List<ApiUser>): List<User> {

        val userList = ArrayList<User>()

        for (apiUser in apiUserList) {
            userList.add(User(apiUser.id, apiUser.firstName, apiUser.lastName))
        }

        return userList

    }

    fun filterUserWhoLovesBoth(cricketFans: List<User>, footballFans: List<User>): List<User> {
        val commonFans = ArrayList<User>()
        for (footballFan in footballFans) {
            if (cricketFans.contains(footballFan))
                commonFans.add(footballFan)
        }

        return commonFans
    }

    fun logError(TAG:String,e:Throwable){

        Log.e(TAG,"onError errorMessage ${e.message}")
    }
}