package com.example.moviecataloguefour.tvshow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class TvShowViewModel: ViewModel() {
    private val listTvShow = MutableLiveData<ArrayList<TvShow>>()

    fun setListTvShow() {
        val list = ArrayList<TvShow>()
        val apikey = "54f1a575ff34a72f82134bf90ea5ff4f"
        val url = "https://api.themoviedb.org/3/discover/tv?api_key=$apikey&language=en-US"
        val client = AsyncHttpClient()
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(TvShowFragment.TAG, result)
                try {
                    val responsObject = JSONObject(result)
                    val resultTvShow = responsObject.getJSONArray("results")

                    for (i in 0 until resultTvShow.length()){
                        val getTvShow = resultTvShow.getJSONObject(i)
                        val tvShow = TvShow()
                        tvShow.title = getTvShow.getString("name")
                        tvShow.description = getTvShow.getString("overview")
                        tvShow.poster = getTvShow.getString("poster_path")
                        list.add(tvShow)
                    }
                    listTvShow.postValue(list)
                }catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                Log.d("onFailur", error.message.toString())
            }
        })
    }

    fun getTvShow(): LiveData<ArrayList<TvShow>>{
        return listTvShow
    }
}