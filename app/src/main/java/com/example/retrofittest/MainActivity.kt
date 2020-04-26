package com.example.retrofittest

import android.app.ProgressDialog
import android.content.ComponentCallbacks2
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofittest.model.Photo
import com.example.retrofittest.model.Posts
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), ComponentCallbacks2 {
    lateinit var progressDialog: ProgressDialog
    val TAG = "TAG"

    companion object {
        lateinit var applicationHandler: Handler
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        applicationHandler = Handler(Looper.getMainLooper())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val service: GetDataService =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading....")
        progressDialog.show()

        val posts = Posts(
            10,
            101,
            "the sunrise",
            "this is the body of posts"
        )

        val postPhoto = service.postPhoto(posts)

        postPhoto.enqueue(object : Callback<Posts> {
            override fun onResponse(call: Call<Posts>?, response: Response<Posts>?) {
                Log.e(TAG, response!!.body().toString())

            }

            override fun onFailure(call: Call<Posts>?, t: Throwable?) {
                Log.e(TAG, "failed")
            }
        })

        val allPhotos = service.getAllPhotos()
        allPhotos.enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>?, response: Response<List<Photo>>?) {
                progressDialog.dismiss()
                generateData(response!!.body()!!)
            }

            override fun onFailure(call: Call<List<Photo>>?, t: Throwable?) {
                progressDialog.dismiss()
                Toast.makeText(
                    this@MainActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun generateData(dataList: List<Photo>) {
        val listView = findViewById<RecyclerView>(R.id.listView)
        listView.layoutManager = LinearLayoutManager(this@MainActivity)

        val subList = ArrayList(dataList/*.subList(1, 4950)*/)
        subList.addAll(dataList)
        (dataList as ArrayList).clear()

        val adapter = CustomAdapter(subList)
        listView.adapter = adapter
    }

    override fun onTrimMemory(level: Int) {
        Log.e(TAG, "onMemory Trim occurred")
        when (level) {

            ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN -> {
                Log.e(TAG, "Release any UI objects that currently hold memory.")

                /*
               Release any UI objects that currently hold memory.
               The user interface has moved to the background.
                */
            }

            ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE,
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW,
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL -> {
                Log.e(TAG, "release any memory that your app doesn't need to run.")

                /*
               Release any memory that your app doesn't need to run.
               The device is running low on memory while the app is running.
               The event raised indicates the severity of the memory-related event.
               If the event is TRIM_MEMORY_RUNNING_CRITICAL, then the system will
               begin killing background processes.
               */
            }

            ComponentCallbacks2.TRIM_MEMORY_BACKGROUND,
            ComponentCallbacks2.TRIM_MEMORY_MODERATE,
            ComponentCallbacks2.TRIM_MEMORY_COMPLETE -> {
                Log.e(TAG, "Release as much memory as the process can.")

                /*
               Release as much memory as the process can.
               The app is on the LRU list and the system is running low on memory.
               The event raised indicates where the app sits within the LRU list.
               If the event is TRIM_MEMORY_COMPLETE, the process will be one of
               the first to be terminated.
               */
            }

            else -> {
                Log.e(TAG, "else.....")
            }
        }
    }
}
