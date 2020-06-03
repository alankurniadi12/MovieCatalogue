package com.example.moviecataloguefour.helper

import android.database.Cursor
import com.example.moviecataloguefour.db.DatabaseContract
import com.example.moviecataloguefour.db.DatabaseContract.FavoriteColums.Companion.POSTER
import com.example.moviecataloguefour.favorite.Favorite
import com.example.moviecataloguefour.movie.Movie

object MappingHelper {
    fun mapCursorToArrayList(movieCursor: Cursor?): ArrayList<Movie>{
        val movieList = ArrayList<Movie>()

        movieCursor?.apply {
            while (moveToNext()){
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColums._ID))
                val title = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColums.TITLE))
                val description = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColums.DESCRIPTION))
                val poster = getString(getColumnIndexOrThrow(POSTER))
                movieList.add(Movie(id, title, description, poster))
            }
        }
        return movieList
    }
}