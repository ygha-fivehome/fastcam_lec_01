package com.ygha.application

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity

class FragmentFirst : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data : String? = arguments?.getString("key")
        Log.d("testt","data is " + data)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate : xml 을 화면에 사용할 준비를 한다.
        // container : fragment에서 사용될 xml의 부모뷰
        val view = inflater.inflate(R.layout.first_fragment, container, false)

        (view.findViewById<TextView>(R.id.call_activity)).setOnClickListener {
            //프래그먼트에서 FragmentActivity의 함수를 호출
            (activity as? FagmentActivity)?.printTestLog()
        }

        return view
    }

    fun printTestLog(){
        Log.d("testt","print test log from fragment")
    }


}