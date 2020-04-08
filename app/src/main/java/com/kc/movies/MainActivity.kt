package com.kc.movies

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kc.movies.model.MovieResponse
import com.kc.movies.service.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    var call = ApiClient.build()?.discover(1)
    call?.enqueue(object : Callback<MovieResponse> {
      override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
        Log.d("TAG", t.message)
      }

      override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
        if (response.body() != null) {
            Log.d("TAG", "response: "+ response.body().toString())
        }
      }
    })
  }
}
