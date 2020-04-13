package com.example.moviecataloguefour.movie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MovieViewModel: ViewModel() {

    private val listMovies = MutableLiveData<ArrayList<Movie>>()

    fun setListMovies() {
        val list = ArrayList<Movie>()
        val apikey = "54f1a575ff34a72f82134bf90ea5ff4f"
        val url = "https://api.themoviedb.org/3/discover/movie?api_key=$apikey&language=en-US"
        val client = AsyncHttpClient()
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(MovieFragment.TAG, result)
                try {
                    val responseObjects = JSONObject(result)
                    val resultMovie = responseObjects.getJSONArray("results")

                    for (i in 0 until resultMovie.length()){
                        val getMovie = resultMovie.getJSONObject(i)
                        val movie = Movie()
                        movie.title = getMovie.getString("title")
                        movie.description = getMovie.getString("overview")
                        movie.poster = getMovie.getString("poster_path")
                        list.add(movie)
                    }
                    listMovies.postValue(list)
                }catch (e: Exception){
                    Log.d("Exception", e.message.toString() )
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                Log.d("onFailur", error.message.toString())
            }
        })
    }

    fun getMovies(): LiveData<ArrayList<Movie>> {
        return listMovies
    }
}