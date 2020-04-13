package com.example.moviecataloguefour.db

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class FavoriteColums: BaseColumns {
        companion object{
            const val TABLE_MOVIE = "movie"
            const val _ID = "_id"
            const val TITLE = "title"
            const val DESCRIPTION = "description"
            const val POSTER = "poster"
        }
    }

    internal class FavoriteColumsTv: BaseColumns {
        companion object {
            const val TABLE_TV = "tvshow"
            const val _ID = "_id"
            const val TITLE = "title"
            const val DESCRIPTION = "description"
            const val POSTER = "poster"
        }
    }
}