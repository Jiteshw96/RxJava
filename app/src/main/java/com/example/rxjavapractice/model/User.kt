package com.example.rxjavapractice.model

data class User(var id :Long = 0L,
                var firstName:String,
                var lastName:String,
                var isFollowing:Boolean = false)
