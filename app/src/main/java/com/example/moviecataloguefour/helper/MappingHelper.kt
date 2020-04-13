package com.example.moviecataloguefour.helper

import android.database.Cursor
import com.example.moviecataloguefour.db.DatabaseContract
import com.example.moviecataloguefour.db.DatabaseContract.FavoriteColums.Companion.POSTER
import com.example.moviecataloguefour.favorite.Favorite

object MappingHelper {
    fun mapCursorToArrayList(favoriteCursor: Cursor?): ArrayList<Favorite>{
        val favoriteList = ArrayList<Favorite>()

        favoriteCursor?.apply {
            while (moveToNext()){
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColums._ID))
                val title = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColums.TITLE))
                val description = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColums.DESCRIPTION))
                val poster = getString(getColumnIndexOrThrow(POSTER))
                favoriteList.add(Favorite(id, title, description, poster))
            }
        }
        return favoriteList
    }
}