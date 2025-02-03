package com.ygha.application.lec_youtube

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.ygha.application.R
import com.ygha.application.lec_retrofit.MelonItem
import com.ygha.application.lec_retrofit.RetrofitService
import com.ygha.application.lec_retrofit.YoutubeItem
import com.ygha.application.mellon.MelonDetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.Serializable

class YoutubeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube)


        val retrofit = Retrofit.Builder().baseUrl("http://mellowcode.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitService = retrofit.create(RetrofitService::class.java)

        retrofitService.getYoutubeItemList().enqueue(object:Callback<ArrayList<YoutubeItem>>{
            override fun onResponse(
                call: Call<ArrayList<YoutubeItem>>,
                response: Response<ArrayList<YoutubeItem>>
            ) {
                /*val youtubeItemList = response.body()
                youtubeItemList!!.forEach {
                    Log.d("youtube",it.title)
                }*/
                val youtubeItemList = response.body()
                val glide= Glide.with(this@YoutubeActivity)
                val adapter = YoutubeItemRecyclerAdapter(
                    youtubeItemList!!,
                    LayoutInflater.from(this@YoutubeActivity),
                    glide,
                    this@YoutubeActivity
                )

                findViewById<RecyclerView>(R.id.youtube_recycler).adapter = adapter


            }

            override fun onFailure(call: Call<ArrayList<YoutubeItem>>, t: Throwable) {
                Log.d("youtube", "fail"+t.message)

            }
        })
    }
}

class YoutubeItemRecyclerAdapter(
    val youtubeItemList:ArrayList<YoutubeItem>,
    val inflater: LayoutInflater,
    val glide: RequestManager,
    val context: Context
): RecyclerView.Adapter<YoutubeItemRecyclerAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title: TextView
        val thumbnail: ImageView
        val content: TextView

        init {
            title = itemView.findViewById(R.id.title)
            thumbnail = itemView.findViewById(R.id.thumbnail)
            content = itemView.findViewById(R.id.content)
            itemView.setOnClickListener {

                val intent = Intent(context, YoutubeItemActivity::class.java)
                /**
                 *  intent로 ArrayList 보내기
                 *  1. Bundle 이용, Parcelable로 만들어서 보내기
                 *  2. serialaizer 이용
                 */

                intent.putExtra("video_url", youtubeItemList.get(adapterPosition).video)



                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.youtube_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = youtubeItemList.get(position).title
        holder.content.text = youtubeItemList.get(position).content
        glide.load(youtubeItemList.get(position).thumbnail).centerCrop().into(holder.thumbnail)
    }

    override fun getItemCount(): Int {
        return youtubeItemList.size
    }
}