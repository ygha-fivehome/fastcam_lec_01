package com.ygha.application.lec_retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ygha.application.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetorfitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retorfit)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://mellowcode.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitService = retrofit.create(RetrofitService::class.java)

        //GET
        retrofitService.getStudentList().enqueue(object : Callback<ArrayList<StudentFromServer>> {
            override fun onResponse(
                call: Call<ArrayList<StudentFromServer>>,
                response: Response<ArrayList<StudentFromServer>>
            ) {
                if(response.isSuccessful){
                    val studentList = response.body()
                    findViewById<RecyclerView>(R.id.recyclerview).apply {
                        this.adapter = StudentListRecyclerViewAdapter(
                            studentList!!,
                            LayoutInflater.from(this@RetorfitActivity)
                        )
                        this.layoutManager = LinearLayoutManager(this@RetorfitActivity)
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<StudentFromServer>>, t: Throwable) {

            }
        })


        //POST, 1번 방식
        findViewById<TextView>(R.id.create).setOnClickListener {

            //1)학생 객체를 보내는 것이 아니라, HashMap으로 해서 보내고 있다.
            val student = HashMap<String, Any>()
            student.put("name","전효정")
            student.put("intro", "펩시")
            student.put("age", 20)

            retrofitService.createStudent(student).enqueue(object :Callback<StudentFromServer>{
                override fun onResponse(
                    call: Call<StudentFromServer>,
                    response: Response<StudentFromServer>
                ) {
                    Log.d("testt",response.message())
                    Log.d("testt",response.errorBody().toString())

                    if(response.isSuccessful){
                        val student = response.body()
                        Log.d("testt","등록한 학생 : " + student!!.name)
                    }
                }

                override fun onFailure(call: Call<StudentFromServer>, t: Throwable) {
                    Log.d("testt","요청 실패")
                }
            })
        }


        //POST, 2번, 학생 객체 전송 방식
        findViewById<TextView>(R.id.easycreate).setOnClickListener {

            val student = StudentFromServer(name="서울", age = 200, intro = "welcome to seoul")
            retrofitService.easyCreateStuent(student).enqueue(object :Callback<StudentFromServer>{
                override fun onResponse(
                    call: Call<StudentFromServer>,
                    response: Response<StudentFromServer>
                ) {
                    Log.d("testt",response.message())
                    Log.d("testt",response.errorBody().toString())

                    if(response.isSuccessful){
                        val student = response.body()
                        Log.d("testt","등록한 학생 : " + student!!.name)
                    }
                }

                override fun onFailure(call: Call<StudentFromServer>, t: Throwable) {
                    Log.d("testt","요청 실패")
                }
            })
        }

    }
}

class StudentListRecyclerViewAdapter(
    var studentList:ArrayList<StudentFromServer>,
    var inflater: LayoutInflater
): RecyclerView.Adapter<StudentListRecyclerViewAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val studentName:TextView
        val studentAge:TextView
        val studentIntro:TextView
        init {
            studentName = itemView.findViewById(R.id.student_name)
            studentAge = itemView.findViewById(R.id.student_age)
            studentIntro = itemView.findViewById(R.id.student_intro)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.student_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.studentName.text = studentList.get(position).name
        holder.studentAge.text = studentList.get(position).age.toString()
        holder.studentIntro.text = studentList.get(position).intro
    }

    override fun getItemCount(): Int {
        return studentList.size
    }
}