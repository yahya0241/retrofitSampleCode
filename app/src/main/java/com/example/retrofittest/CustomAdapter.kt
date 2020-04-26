package com.example.retrofittest

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofittest.model.Photo
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class CustomAdapter(private var dataList: List<Photo>) :
    RecyclerView.Adapter<CustomAdapter.CustomViewHolder>() {

    private val okHttpClient = OkHttpClient()
    private val retrofitInstance =
        RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var mView: View = itemView

        var textView: TextView
        var coverImage: ImageView

        init {
            textView = mView.findViewById(R.id.title)
            coverImage = mView.findViewById(R.id.coverImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.custom_row, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.textView.text = dataList[position].title

        downloadImageWithRetrofit(position, holder)
//        downLoadImageWithOkHttp(position, holder)
    }

    private fun downloadImageWithRetrofit(
        position: Int,
        holder: CustomViewHolder
    ) {
        val photo = retrofitInstance.getPhoto(dataList[position].thumbnailUrl)
        photo.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                val bitmap = BitmapFactory.decodeStream(response!!.body()!!.byteStream())
                MainActivity.applicationHandler.post { holder.coverImage.setImageBitmap(bitmap) }
            }

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                println("Retrofit onFailure")
            }
        })
    }

    private fun downLoadImageWithOkHttp(
        position: Int,
        holder: CustomViewHolder
    ) {
        val request = Request.Builder().url(dataList[position].thumbnailUrl).build()
        okHttpClient.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call?, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    val bitmap = BitmapFactory.decodeStream(response.body()!!.byteStream())
                    MainActivity.applicationHandler.post { holder.coverImage.setImageBitmap(bitmap) }
                } else {
                    println("OkHttp in not successful")
                }
            }

            override fun onFailure(call: okhttp3.Call?, e: IOException?) { //Handle the error
                println("OkHttp onFailure")
            }
        })
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}