package com.ygha.application.instagram

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.ygha.application.R
import com.ygha.application.lec_retrofit.RetrofitService
import com.ygha.application.lec_retrofit.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InstaLoginActivity : AppCompatActivity() {

    var username:String=""
    var password:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insta_login)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://mellowcode.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitService = retrofit.create(RetrofitService::class.java)


        //아이디
        findViewById<EditText>(R.id.id_input).doAfterTextChanged {
            username = it.toString()
        }


        //패스워드
        findViewById<EditText>(R.id.pw_input).doAfterTextChanged {
            password = it.toString()
        }


        //가입하기
        findViewById<TextView>(R.id.insta_join).setOnClickListener {
            startActivity(Intent(this, InstalJoinActivity::class.java))
        }

        //로그인 버튼을 누르면
        /**
         *
         * class UserToken(
         *     val username:String,
         *     val token: String
         * )
         *
         *
         *  @POST("user/login/")
         *     @FormUrlEncoded
         *     fun instaLogin(
         *         @FieldMap params: HashMap<String, Any>
         *     ):Call<UserToken>
         *
         *         을 하면 결과값 200이 오고, token 값이 온다.
         */
        findViewById<TextView>(R.id.login_btn).setOnClickListener {
            val user = HashMap<String, Any>()
            user.put("username", username)
            user.put("password", password)
            retrofitService.instaLogin(user).enqueue(object : Callback<User>{

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if(response.isSuccessful){
                        Log.d("testt","token is " +  user)
                        val user:User = response.body()!!
                        val userToken = response.body()!!
                        val sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE)
                        val editor :SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putString("token", user.token!!)
                        editor.putString("user_id", user.id.toString())
                        editor.commit()
                        startActivity(Intent(this@InstaLoginActivity, InstaMainActivity::class.java))
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d("testt","token is" + t.message)
                }
            })

        }

    }
}