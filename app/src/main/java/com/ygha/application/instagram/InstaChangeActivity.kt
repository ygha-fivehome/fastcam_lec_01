package com.ygha.application.instagram

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.ygha.application.R
import com.ygha.application.lec_retrofit.RetrofitService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class InstaChangeActivity : AppCompatActivity() {

    var imageUri: Uri? = null
    var glide:RequestManager?=null
    lateinit var imageView:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insta_change)

        imageView = findViewById(R.id.profile_img)

        glide = Glide.with(this)

        val imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                imageUri = it.data!!.data
                glide!!.load(imageUri).into(imageView)
                Log.d("insta", ""+imageUri)

                //glide.load(imageUri).into(selectedImageView)
            }


        imagePickerLauncher.launch(
            Intent(Intent.ACTION_PICK).apply {
                this.type = MediaStore.Images.Media.CONTENT_TYPE
            }
        )

        findViewById<TextView>(R.id.change_img).setOnClickListener {
            val file = getRealFile(imageUri!!)
            val requestFile = RequestBody.create(
                MediaType.parse(
                    (this as InstaMainActivity).contentResolver.getType(imageUri!!)
                ), file
            )
            val body = MultipartBody.Part.createFormData("image", file!!.name, requestFile)
            val header = HashMap<String, String>()
            val sp = this.getSharedPreferences(
                "user_info",
                Context.MODE_PRIVATE
            )
            val token = sp.getString("token", "")
            header.put("Authorization","token " + token!!)

            val userId = sp.getString("user_id", "")!!.toInt()

            val retrofit = Retrofit.Builder()
                .baseUrl("http://mellowcode.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val retrofitService = retrofit.create(RetrofitService::class.java)
            val user = RequestBody.create(MultipartBody.FORM, userId.toString())
            retrofitService.changeProfile(userId!!, header,body, user).enqueue(object :Callback<Any>{
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if(response.isSuccessful){
                        Toast.makeText(this@InstaChangeActivity, "변경완료",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    Toast.makeText(this@InstaChangeActivity, "변경실패",Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }

            })


        }
    }


    private fun getRealFile(uri:Uri) : File?{
        var uri:Uri? = uri
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        if(uri==null){
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        var cursor: Cursor?=(this as InstaMainActivity).contentResolver.query(
            uri!!,
            projection,
            null,
            null,
            MediaStore.Images.Media.DATE_MODIFIED + " desc"
        )

        if(cursor == null || cursor.columnCount<1){
            return null
        }
        val column_index : Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val path : String = cursor.getString(column_index)
        if(cursor!=null){
            cursor.close()
            cursor =  null
        }

        return File(path)
    }
}