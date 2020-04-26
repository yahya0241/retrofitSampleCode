package com.example.retrofittest

import com.example.retrofittest.model.Photo
import com.example.retrofittest.model.Posts
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface GetDataService {

    @GET("/photos")
    fun getAllPhotos(): Call<List<Photo>>

    @GET
    @Streaming
    fun getPhoto(@Url url: String): Call<ResponseBody>

    @POST("/posts")
    fun postPhoto(@Body posts: Posts): Call<Posts>
}