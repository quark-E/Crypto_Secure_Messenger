package com.example.crypto_secure_messenger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.widget.TextView;
import org.w3c.dom.Text
import kotlinx.android.synthetic.main.activity_main.hello

class MainActivity : AppCompatActivity() {

    //val web = Web3_Stuff()

    override fun onCreate(savedInstanceState: Bundle?) {
        //val web = Web3_Stuff()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val web = Web3_Stuff(this)
        val hello: TextView = findViewById(R.id.hello) as TextView
        web.Views(this, hello)
    }
}
