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
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val EXTRA_FAV_MOVIE = "extra_fav_movie"
        const val EXTRA_POSITION = "extra_position"
        const val RESULT_DELETE = 102
        const val REQUEST_UPDATE = 103
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

        adapter = FavoriteMovieAdapter(activity!!)
        adapter.notifyDataSetChanged()

        showRecyclerViewFavMov()

        /*val intent = Intent(context, DetailMovieActivity::class.java)
        startActivityForResult(intent, DetailMovieActivity.REQUEST_ADD)*/

        movieHelper = MovieHelper.getInstance(context)
        movieHelper.open()

        loadFavoriteAsync()
    }



    private fun loadFavoriteAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBarFavMov.visibility = View.VISIBLE
            val deferredFavorite = async(Dispatchers.IO) {
                val cursor = movieHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressBarFavMov.visibility = View.INVISIBLE
            val favorites = deferredFavorite.await()
            if (favorites.size > 0) {
                adapter.listFavMovie = favorites
            }else {
                adapter.listFavMovie = ArrayList()
                showSnackbarMessage("Empty favorite list")
            }
        }
    }

    private fun showRecyclerViewFavMov() {
        rv_item_fav_mov.layoutManager = LinearLayoutManager(context)
        rv_item_fav_mov.adapter = adapter

        adapter.setData(list)

    }


    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_item_fav_mov, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        movieHelper.close()
    }

}
