package com.example.crypto_secure_messenger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.view.Menu

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        val web = Web3_Stuff(this)
        val address: TextView = findViewById(R.id.Address) as TextView
        web.Views(this, address)

        val btn = findViewById(R.id.button) as Button
        val sendAddy = findViewById(R.id.sendAddress) as EditText
        val msgFill = findViewById(R.id.msgFill) as EditText

        btn.setOnClickListener {web.sendTrans(sendAddy.text.toString(), msgFill.text.toString())}
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }
}
