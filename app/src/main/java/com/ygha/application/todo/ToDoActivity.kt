package com.ygha.application.todo

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.ygha.application.R
import com.ygha.application.lec_retrofit.RetrofitService
import com.ygha.application.lec_retrofit.ToDo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ToDoActivity : AppCompatActivity() {

    lateinit var todoRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do)


        findViewById<ImageView>(R.id.write).setOnClickListener {
            startActivity(Intent(this, ToDoWriteActivity::class.java))
        }

        todoRecyclerView = findViewById<RecyclerView>(R.id.todo_list)

        getToDoList()

        findViewById<EditText>(R.id.search_edittext).doAfterTextChanged {
            searchToDoList(it.toString())
        }
    }


    fun searchToDoList(keyword:String){
        val retrofit = Retrofit.Builder()
            .baseUrl("http://mellowcode.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitService = retrofit.create(RetrofitService::class.java)

        val header = HashMap<String,String>()
        val sp = this.getSharedPreferences(
            "user_info",
            Context.MODE_PRIVATE
        )
        val token = sp.getString("token", "")
        header.put("Authorization","token " + token!!)

        retrofitService.searchToDoList(header, keyword).enqueue(object : Callback<ArrayList<ToDo>>{
            override fun onResponse(
                call: Call<ArrayList<ToDo>>,
                response: Response<ArrayList<ToDo>>
            ) {
                if(response.isSuccessful){
                    val todoList = response.body()
                    makeToDoList(todoList!!)
                }
            }

            override fun onFailure(call: Call<ArrayList<ToDo>>, t: Throwable) {

            }
        })
    }


    fun makeToDoList(todoList:ArrayList<ToDo>){
        todoRecyclerView.adapter =
            ToDoListRecyclerViewAdapter(
                todoList!!, LayoutInflater.from(this@ToDoActivity), this@ToDoActivity
            )

    }


    //리스트 완료 상태로 바꾸기
    fun changeToDoComplete(todoId:Int, activity: ToDoActivity){
        val retrofit = Retrofit.Builder()
            .baseUrl("http://mellowcode.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitService = retrofit.create(RetrofitService::class.java)

        val header = HashMap<String,String>()
        val sp = this.getSharedPreferences(
            "user_info",
            Context.MODE_PRIVATE
        )
        val token = sp.getString("token", "")
        header.put("Authorization","token " + token!!)

        retrofitService.changeToDoComplete(header, todoId).enqueue(object : Callback<Any>{
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                activity.getToDoList()
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                activity.getToDoList()
            }
        })
    }




    fun getToDoList(){
        val retrofit = Retrofit.Builder()
            .baseUrl("http://mellowcode.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitService = retrofit.create(RetrofitService::class.java)

        val header = HashMap<String,String>()
        val sp = this.getSharedPreferences(
            "user_info",
            Context.MODE_PRIVATE
        )
        val token = sp.getString("token", "")
        header.put("Authorization","token " + token!!)



        retrofitService.getToDoList(header).enqueue(object : Callback<ArrayList<ToDo>>{
            override fun onResponse(
                call: Call<ArrayList<ToDo>>,
                response: Response<ArrayList<ToDo>>
            ) {
                if(response.isSuccessful){
                    val todoList = response.body()
                    makeToDoList(todoList!!)
                }
            }

            override fun onFailure(call: Call<ArrayList<ToDo>>, t: Throwable) {

            }
        })
    }
}




class ToDoListRecyclerViewAdapter(
    val todoList:ArrayList<ToDo>,
    val inflater: LayoutInflater,
    val activity: ToDoActivity
):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var previousDate:String=""

    inner class DateViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val dataTextView:TextView


        init {
            dataTextView = itemView.findViewById(R.id.date)
        }
    }

    inner class ContentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val content:TextView
        val isComplete:ImageView

        init {
            content = itemView.findViewById(R.id.content)
            isComplete = itemView.findViewById(R.id.is_complete)
            isComplete.setOnClickListener {

                /* 아래와 같이 요청 하면 아래 두 줄이 순서가 보장 안된다.
                activity.changeToDoComplete(todoList.get(adapterPosition).id)
                activity.getToDoList()

                그래서 아래로 옮긴다.
                 retrofitService.changeToDoComplete(header, todoId).enqueue(object : Callback<Any>{
                    override fun onResponse(call: Call<Any>, response: Response<Any>) {
                        activity.getToDoList()
                    }

                    override fun onFailure(call: Call<Any>, t: Throwable) {

                    }
                })
                 */

                activity.changeToDoComplete(todoList.get(adapterPosition).id, activity)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        //날짜에 따라 구분
        val todo = todoList.get(position)
        //날짜와 초분초가 다 들어 있다. 날짜로 구분하기 위해 잘라내야 한다. T를 기준으로 잘라 낸다.
        //샘플 -->  "created" : "2022-03-04T11:37:17.393872734Z"
        val tempDate = todo.created.split("T")[0]//T를 기준으로 앞쪽이라서 0 번째

        if(previousDate == tempDate){
            return 0
        }else{
            previousDate = tempDate
            return 1
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            1-> return DateViewHolder(inflater.inflate(R.layout.todo_date, parent,false))
            else -> return ContentViewHolder(inflater.inflate(R.layout.todo_content, parent,false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val todo = todoList.get(position)
        if(holder is DateViewHolder){
            (holder as DateViewHolder).dataTextView.text = todo.created.split("T")[0]
        }else{
            (holder as ContentViewHolder).content.text = todo.content
            if(todo.is_complete){
                (holder as ContentViewHolder).isComplete.setImageDrawable(activity.resources.getDrawable(
                    R.drawable.btn_radio_check,
                    activity.theme))
            }else {
                (holder as ContentViewHolder).isComplete.setImageDrawable(activity.resources.getDrawable(
                    R.drawable.btn_radio,
                    activity.theme))
            }

        }
    }


    override fun getItemCount(): Int {
        return todoList.size
    }
}