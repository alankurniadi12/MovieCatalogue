package com.example.moviecataloguefour.movie

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.moviecataloguefour.R
import com.example.moviecataloguefour.db.DatabaseContract.FavoriteColums.Companion.DESCRIPTION
import com.example.moviecataloguefour.db.DatabaseContract.FavoriteColums.Companion.POSTER
import com.example.moviecataloguefour.db.DatabaseContract.FavoriteColums.Companion.TITLE
import com.example.moviecataloguefour.db.MovieHelper
import com.example.moviecataloguefour.favorite.Favorite
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_detail_movie.*
import kotlinx.android.synthetic.main.fragment_favorite_movie.*
import kotlinx.android.synthetic.main.item_fav_mov.*

class DetailMovieActivity : AppCompatActivity() {

    private var isFav = false
    private var menuItem: Menu? = null
    private var movie: Movie? = null
    private var position: Int = 0
    private lateinit var movieHelper: MovieHelper


    companion object {
        const val EXTRA_MOVIE = "extra_movie"
        const val EXTRA_POSITION = "extra_position"
        const val RESULT_ADD = 11
        const val RESULT_DELETE = 12
        const val REQUEST_ADD = 14
        const val REQUEST_UPDATE = 15
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)


        setIconFav()

        showLoading(true)
        getDataMovies()
    }

    private fun setIconFav() {
        if (isFav){
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_black_24dp)
        }else{
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_black_24dp)
        }
    }

    private fun showLoading (state: Boolean){
        if (state){
            progressbar_movie_detail.visibility = View.VISIBLE
        }else{
            progressbar_movie_detail.visibility = View.GONE
        }
    }

    private fun getDataMovies() {
        val movie = intent.getParcelableExtra(EXTRA_MOVIE) as Movie
        tv_detail_title_movie.text = movie.title
        tv_detail_desc_movie.text = movie.description
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w154/"+movie.poster)
            .into(img_detail_movie)
        showLoading(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add_favorite ->
                if (isFav){
                    showAlertDialog()
                }else {
                    addFavorite()
                }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAlertDialog() {
        val dialogTitle = "Remove"
        val dialogMessage = "Remove from list favorite"

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Yes") {dialog, id ->
                    val result = movieHelper.deleteById(movie?.id.toString()).toLong()
                    if (result > 0){
                        val intent = Intent()
                        intent.putExtra(EXTRA_POSITION, position)
                        setResult(RESULT_DELETE, intent)
                        finish()
                    }else{
                        Toast.makeText(this@DetailMovieActivity, "Faild remove item", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("No") {dialog, id -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun addFavorite() {
        val title = tv_item_title_favorite.toString()
        val desc = tv_item_desc_favorite.toString()

        movie?.title = title
        movie?.description = desc
        Glide.with(this).load("https://image.tmdb.org/t/p/w154/"+movie?.poster).into(img_item_favorite)

        val intent = Intent()
        intent.putExtra(EXTRA_MOVIE, movie)
        intent.putExtra(EXTRA_POSITION, position)

        val values = ContentValues()
        //values.put(_ID, movie?.id)
        values.put(TITLE, title)
        values.put(DESCRIPTION, desc)
        values.put(POSTER, movie?.poster)

        val result = movieHelper.insert(values)
        if (result > 0){
            movie?.id = result.toInt()
            setResult(RESULT_ADD, intent)
            finish()
        } else{
            Toast.makeText(this, "Faild add item", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        movieHelper.close()
    }

}
