package com.ygha.application.lec_retrofit

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.File
import java.io.Serializable


class StudentFromServer(
    val id:Int,
    val name:String,
    val age:Int,
    val intro:String
){
    constructor(name:String, age:Int, intro:String):this(0,name, age, intro)
}

class User(
    val username:String,
    val token: String,
    val id:String
)

@Parcelize
data class MelonItem(
    val id:Int, val title:String, val song:String, val thumbnail:String
):Parcelable


class YoutubeItem(
    val id:Int, val title: String, val content:String, val video:String, val thumbnail: String
):Serializable


class InstaPost(
    val id:Int,
    val content: String,
    val image:String,
    val owner_profile:OwerProfile
)

class OwerProfile(
    val username: String,
    val image:String?
)
class Post(
    val content: String, image: File
)

class UserInfo(
    val id:Int,
    val username: String,
    val profile: OwerProfile
)


class ToDo(
    val id:Int,
    val content: String,
    val is_complete:Boolean,
    val created:String
)

interface RetrofitService {

    @GET("to-do/search/")
    fun searchToDoList(
        @HeaderMap headerMap: Map<String, String>,
        @Query("keyword") keyword:String
    ):Call<ArrayList<ToDo>>


    @PUT("to-do/complete/{todoId}")
    fun changeToDoComplete(
        @HeaderMap headerMap: Map<String, String>,
        @Path("todoId") todoId:Int

    ):Call<Any>


    @GET("to-do/")
    fun getToDoList(
        @HeaderMap headers: Map<String, String>,
    ):Call<ArrayList<ToDo>>

    @POST("to-do/")
    @FormUrlEncoded
    fun makeTodo(
        @HeaderMap headers:Map<String, String>,
        @FieldMap params: HashMap<String, Any>
    ):Call<Any>




    @Multipart
    @PUT("user/profile/{user_id}/")
    fun changeProfile(
        @Path("user_id") userId:Int,
        @HeaderMap headers:Map<String, String>,
        @Part image:MultipartBody.Part,
        @Part("user") user:RequestBody
    ):Call<Any>



    @GET("user/userInfo/")
    fun getUserInfo(
        @HeaderMap headers: Map<String, String>,
    ):Call<UserInfo>

    //POST UPLOAD
    //사진을 보낼 때, 파일을 보낼 때 @Multipart 사용
    @POST("instagram/post/")
    @Multipart
    fun uploadPost(
        @HeaderMap headers: Map<String, String>,
        @Part image:MultipartBody.Part,
        @Part("content") content: RequestBody

    ):Call<Any>


    @POST("instagram/post/like/{post_id}/")
    fun postLike(
        @Path("post_id") post_id:Int
    ):Call<Any>


    @GET("instagram/post/list/all/")
    fun getInstagramPosts(
    ):Call<ArrayList<InstaPost>>


    @GET("melon/list/")
    fun getmelonItemList():Call<ArrayList<MelonItem>>

    @POST("user/signup/")
    @FormUrlEncoded
    fun instaJoin(
        @FieldMap params: HashMap<String, Any>
    ):Call<User>

    @GET("youtube/list/")
    fun getYoutubeItemList():Call<ArrayList<YoutubeItem>>

    //http://mellowcode.org/user/login/?username=fivehome&password=123123
    @POST("user/login/")
    @FormUrlEncoded
    fun instaLogin(
        @FieldMap params: HashMap<String, Any>
    ):Call<User>

    // get/post 에 대해 url이 같을 수도 있다.

    @GET("json/students/")
    fun getStudentList(): Call<ArrayList<StudentFromServer>>

    // post 1번 방식, student 객체가 아닌 HashMap으로 보낸다.
    @POST("json/students/")
    fun createStudent(
        @Body params:HashMap<String, Any>
    ):Call<StudentFromServer>

    // post 2번 방식, 학생 객체 자체를 보내는 방식
    @POST("json/students/")
    fun easyCreateStuent(
        @Body student: StudentFromServer
    ):Call<StudentFromServer>


}