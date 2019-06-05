package com.example.crypto_secure_messenger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Context

class MainActivity : AppCompatActivity() {

    //val web = Web3_Stuff()

    override fun onCreate(savedInstanceState: Bundle?) {
        //val web = Web3_Stuff()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val web = Web3_Stuff(this)
    }
}
