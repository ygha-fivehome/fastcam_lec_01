package com.ygha.application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class FagmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fagment_test)

        val fragmentManager = supportFragmentManager
        val fragmentFirst = FragmentFirst()


        //Transaction
        /*
         - 작업의 단위이므로 시작과 끝을 명시해야한다.
         begin(시작) , commit(완료)
         Commit 의 종류
        1. commit  2. commitAllowingStateLoss  3. commitNow    4. commitNowAllowStateLoss

        commit - commitAllowStateLoss
        - 상태를 손실을 허용한다.
        - 상태손실 : os 에서 메모리 등이 부족하면 os가 여러가지 처리를 한다. 이때 손실을 허용한다.
          onStop, lifecycle 종료에 따라 앱이 상태를 저장해야 할 수도 있다.
        - 상태저장은 onSaveInstanceState에 담는다.
        - commit 일때는 저장을 한 경우 실행할 수 없다.(IllegalStateException발생)
        - commitAllowinngStateLosss는 저장을 한 경우 예외가 발생하지 않고 그냥 손실


        commit - commitNow         */



        // 2. 코드상에서 Fragment를 붙이고 제거하기
        (findViewById<TextView>(R.id.add)).setOnClickListener {
            val transaction = fragmentManager.beginTransaction() //시작
            // 3. 프래그먼트에 데이터를 전달하는 방법
            // 프래그먼트의 경우, Bundle을 이용해서 전달 한다.

            val bundle=Bundle()
            bundle.putString("key", "hello")
            fragmentFirst.arguments = bundle


            transaction.replace(R.id.root, fragmentFirst, "fragment_first_tag") // replace 시킨다.
            transaction.commit() //완료, commit 해야 작업이 시작되어 완료 된다.
        }

        (findViewById<TextView>(R.id.remove)).setOnClickListener {
            val transaction = fragmentManager.beginTransaction()
            transaction.remove(fragmentFirst)
            transaction.commit()
        }


        (findViewById<TextView>(R.id.access_fragment)).setOnClickListener {
            //activity에서 fragment로 access 하는 방법
            // fragment를 붙이는 방법은 xml로 하는 방법, 코드로 하는 방법 아래는 두 가지 방법별로 fragment를
            // 찾는 방법을 설명한다.

            //1. xml 에 있는 fragment를 찾는 방법, if 를 이용한다.
            val fragmentFirst = supportFragmentManager.findFragmentById(R.id.fragment_first) as FragmentFirst
            fragmentFirst.printTestLog()

            //2. 코드로 fragment로 붙인 경우에 fragment를 찾는 방법, tag 를 이용한다.
            supportFragmentManager.findFragmentByTag("fragment_frist_tag") as FragmentFirst
            fragmentFirst.printTestLog()

        }

    }

    fun printTestLog(){
        Log.d("testt","print test log")
    }
}