package com.ygha.application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ygha.application.databinding.ActivityMainBinding

//ViewBinding : Databinding 보다는 편하나, 양방향이 안된다.
 // Activity
 // Fragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)




    }
}

class Car(val nthCar:String, val nthEngine:String)