package com.example.rxjavapractice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun startOperatorActivity(view: View) {
        startActivity(Intent(this@MainActivity,OperatorsActivity::class.java))
    }
    fun startNetworkingActivity(view: View) {

    }
    fun startSearchActivity(view: View) {
        startActivity(Intent(this@MainActivity,SearchActivity::class.java))
    }

}