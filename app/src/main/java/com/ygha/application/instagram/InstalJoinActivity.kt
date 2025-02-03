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
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InstalJoinActivity : AppCompatActivity() {

    var username:String=""
    var password1:String=""
    var password2:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instal_join)



        findViewById<TextView>(R.id.insta_login).setOnClickListener {
            startActivity(Intent(this, InstaLoginActivity::class.java))
        }

        findViewById<EditText>(R.id.id_input).doAfterTextChanged { username=it.toString() }
        findViewById<EditText>(R.id.pw_input1).doAfterTextChanged { password1=it.toString() }
        findViewById<EditText>(R.id.pw_input2).doAfterTextChanged { password2=it.toString() }


        val retrofit = Retrofit.Builder()
            .baseUrl("http://mellowcode.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitService = retrofit.create(RetrofitService::class.java)
        val user = HashMap<String, Any>()


        findViewById<TextView>(R.id.join_btn).setOnClickListener {
            user.put("username", username)
            user.put("password1", password1)
            user.put("password2", password2)

            retrofitService.instaJoin(user).enqueue(object :Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if(response.isSuccessful){
                        val user = response.body()!!
                        Log.d("testt","token is " +  user)
                        val sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE)
                        val editor :SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putString("token", user.token!!)
                        editor.putString("user_id", user.id.toString())
                        editor.commit()
                        startActivity(Intent(this@InstalJoinActivity, InstaMainActivity::class.java))
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d("testt", t.message.toString())
                }
            })

        }


    }
}