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
import com.example.moviecataloguefour.db.DatabaseContract.FavoriteColums.Companion._ID
import com.example.moviecataloguefour.db.MovieHelper
import com.example.moviecataloguefour.favorite.Favorite
import com.example.moviecataloguefour.favorite.FavoriteMovieAdapter
import com.example.moviecataloguefour.favorite.FavoriteMovieFragment
import com.example.moviecataloguefour.favorite.FavoriteMovieFragment.Companion.EXTRA_FAV_MOVIE
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_detail_movie.*
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.fragment_favorite_movie.*
import kotlinx.android.synthetic.main.item_fav_mov.*

class DetailMovieActivity : AppCompatActivity() {

    private var isFav = false
    private var menuItem: Menu? = null
    private var favorite: Favorite? = null
    private var position: Int = 0
    private lateinit var movieHelper: MovieHelper
    private lateinit var adapter: FavoriteMovieAdapter

    companion object {
        const val EXTRA_MOVIE = "extra_movie"
        const val ALERT_DIALOG_DELETE = 10
        const val EXTRA_POSITION = "extra_position"
        const val RESULT_ADD = 11
        const val RESULT_DELETE = 12
        const val REQUEST_ADD = 14
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        adapter = FavoriteMovieAdapter(this)


        /*val intent = Intent(this, FavoriteMovieFragment::class.java)
        startActivityForResult(intent, FavoriteMovieFragment.REQUEST_ADD)*/

        movieHelper = MovieHelper.getInstance(applicationContext)
        movieHelper.open()

        //favorite = intent.getParcelableExtra(EXTRA_FAV_MOVIE)
        if (favorite != null){
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isFav = false
        }else{
            favorite = Favorite()
        }

        setIconFav()

        showLoading(true)
        getDataMovies()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null)
            when (requestCode) {
                FavoriteMovieFragment.REQUEST_ADD ->
                    if (resultCode == FavoriteMovieFragment.RESULT_ADD) {
                        val favorite = data.getParcelableExtra<Favorite>(EXTRA_FAV_MOVIE)

                        adapter.addItem(favorite)
                        rv_item_fav_mov.smoothScrollToPosition(adapter.itemCount - 1)

                        showSnackbarMessage("1 item added")
                    }
                FavoriteMovieFragment.RESULT_DELETE -> {
                    val position = data.getIntExtra(FavoriteMovieFragment.EXTRA_POSITION, 0)

                    adapter.removeItem(position)

                    showSnackbarMessage("1 item removed")
                }
            }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_item_fav_mov, message, Snackbar.LENGTH_SHORT).show()
    }


    private fun setIconFav() {
        if (isFav == true){
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
        /*menuItem = menu
        setIconFav()*/

        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add_favorite ->
                if (isFav == true){
                    showAlertDialog(ALERT_DIALOG_DELETE)
                }else {
                    addFavorite()
                }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun showAlertDialog(type: Int) {
        val isDialogDelete = type == ALERT_DIALOG_DELETE
        val dialogTitle = "Remove"
        val dialogMessage = "Remove from list favorite"
        val alertDialogBuilder = AlertDialog.Builder(this)

        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Yes") {dialog, id ->
                if (isDialogDelete) {
                    finish()
                }else {
                    val result = movieHelper.deleteById(favorite?.id.toString()).toLong()
                    if (result > 0){
                        val intent = Intent()
                        intent.putExtra(EXTRA_POSITION, position)
                        setResult(RESULT_DELETE, intent)
                        finish()
                    }else{
                        Toast.makeText(this@DetailMovieActivity, "Faild remove item", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("No") {dialog, id -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


    private fun addFavorite() {

        val intent = Intent()
        intent.putExtra(EXTRA_FAV_MOVIE, favorite)
        intent.putExtra(EXTRA_POSITION, position)

        val values = ContentValues()
        values.put(_ID, favorite?.id)
        values.put(TITLE, favorite?.title)
        values.put(DESCRIPTION, favorite?.description)
        values.put(POSTER, favorite?.poster)

        movieHelper.open()
        val result = movieHelper.insert(values)
        if (result > 0){
            favorite?.id = result.toInt()
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
