package com.ygha.application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ApplicationActivityTwo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application_two)

        findViewById<TextView>(R.id.changeActivity).setOnClickListener {
            startActivity(
                Intent(this, ApplicationActivityOne::class.java)
            )
        }
    }
}