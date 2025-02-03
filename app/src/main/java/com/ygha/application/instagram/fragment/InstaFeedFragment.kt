package com.ygha.application.instagram.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.ygha.application.R
import com.ygha.application.lec_retrofit.InstaPost
import com.ygha.application.lec_retrofit.Post
import com.ygha.application.lec_retrofit.RetrofitService
import com.ygha.application.lec_retrofit.YoutubeItem
import com.ygha.application.lec_youtube.YoutubeItemActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InstaFeedFragment : Fragment() {

    lateinit var retrofitService:RetrofitService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.insta_feed_fragment, container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val feedListView = view.findViewById<RecyclerView>(R.id.feed_list)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://mellowcode.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofitService = retrofit.create(RetrofitService::class.java)

        retrofitService.getInstagramPosts().enqueue(object : Callback<ArrayList<InstaPost>> {
            override fun onResponse(
                call: Call<ArrayList<InstaPost>>,
                response: Response<ArrayList<InstaPost>>
            ) {
                val postList = response.body()
                /*postList!!.forEach{
                    Log.d("insta", it.content)
                }*/
                val postRecyclerView = view.findViewById<RecyclerView>(R.id.feed_list)
                postRecyclerView.adapter = PostRecyclerViewAdapter(
                    postList!!,
                    LayoutInflater.from(activity),
                    Glide.with(activity!!),
                    this@InstaFeedFragment
                )
            }

            override fun onFailure(call: Call<ArrayList<InstaPost>>, t: Throwable) {

            }
        })
    }

    fun postLike(post_id:Int){
        retrofitService.postLike(post_id).enqueue(object :Callback<Any>{
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                Toast.makeText(activity, "좋아요", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Toast.makeText(activity, "실패", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

class PostRecyclerViewAdapter(
    val postList:ArrayList<InstaPost>,
    val inflater: LayoutInflater,
    val glide:RequestManager,
    val instaFeedFragment : InstaFeedFragment
): RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val ownerImage:ImageView
        val owenerUsername:TextView
        val postImag:ImageView
        val postContent:TextView
        init {
            ownerImage = itemView.findViewById(R.id.owner_img)
            owenerUsername = itemView.findViewById(R.id.owner_username)
            postImag = itemView.findViewById(R.id.post_img)
            postContent = itemView.findViewById(R.id.post_content)

            postImag.setOnClickListener {
                instaFeedFragment.postLike(postList.get(adapterPosition).id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.post_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = postList.get(position)

        post.owner_profile.image.let {//image가 null 이 아닌 경우에만 작업 한다.
            glide.load(it).into(holder.ownerImage)
        }

        post.image.let {
            glide.load(it).into(holder.postImag)
        }
        holder.owenerUsername.text = post.owner_profile.username
        holder.postContent.text = post.content
    }

    override fun getItemCount(): Int {
        return postList.size
    }
}
