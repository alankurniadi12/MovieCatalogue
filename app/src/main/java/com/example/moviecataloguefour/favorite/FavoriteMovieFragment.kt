package com.example.moviecataloguefour.favorite

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviecataloguefour.CustomOnItemClickListener

import com.example.moviecataloguefour.R
import com.example.moviecataloguefour.db.MovieHelper
import com.example.moviecataloguefour.helper.MappingHelper
import com.example.moviecataloguefour.movie.DetailMovieActivity
import com.example.moviecataloguefour.movie.Movie
import com.example.moviecataloguefour.movie.MovieAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_favorite_movie.*
import kotlinx.android.synthetic.main.item_fav_mov.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class FavoriteMovieFragment : Fragment() {

    private lateinit var adapter: FavoriteMovieAdapter
    private lateinit var movieHelper: MovieHelper
    private val list = ArrayList<Favorite>()

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieHelper = MovieHelper.getInstance(context)
        movieHelper.open()

        showRecyclerFavMov()

        val intent = Intent(context, DetailMovieActivity::class.java)
        startActivityForResult(intent, DetailMovieActivity.REQUEST_ADD)

        if (savedInstanceState == null) {
            loadFavoriteAsync()
        }else {
            val list = savedInstanceState.getParcelableArrayList<Movie>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavMovie = list
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.mData)
    }

    private fun showRecyclerFavMov() {
        rv_item_fav_mov.layoutManager = LinearLayoutManager(context)
        rv_item_fav_mov.setHasFixedSize(true)
        adapter = MovieAdapter(activity!!)
        rv_item_fav_mov.adapter = adapter

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                DetailMovieActivity.REQUEST_ADD ->
                    if (resultCode == DetailMovieActivity.RESULT_ADD) {
                        val movie = data.getParcelableExtra<Movie>(DetailMovieActivity.EXTRA_MOVIE)

                        adapter.addItem(movie)
                        rv_item_fav_mov.smoothScrollToPosition(adapter.itemCount - 1)

                        showSnackbarMessage("1 item added")
                    }
                DetailMovieActivity.REQUEST_UPDATE -> {
                    when (requestCode) {
                        DetailMovieActivity.RESULT_DELETE -> {
                            val position = data.getIntExtra(DetailMovieActivity.EXTRA_POSITION, 0)

                            adapter.removeItem(position)

                            showSnackbarMessage("1 item removed")
                        }
                    }
                }
            }
        }
    }

    private fun loadFavoriteAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBarFavMov.visibility = View.VISIBLE
            val deferredFavMovie = async(Dispatchers.IO) {
                val cursor = movieHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressBarFavMov.visibility = View.INVISIBLE
            val movies = deferredFavMovie.await()
            if (movies.size > 0) {
                adapter.mData = movies
            }else {
                adapter.mData = ArrayList()
                showSnackbarMessage("Empty favorite list")
            }
        }
    }



    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_item_fav_mov, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        movieHelper.close()
    }

}
