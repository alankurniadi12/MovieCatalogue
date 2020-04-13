package com.example.moviecataloguefour.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.moviecataloguefour.db.DatabaseContract.FavoriteColums.Companion.DESCRIPTION
import com.example.moviecataloguefour.db.DatabaseContract.FavoriteColums.Companion.POSTER
import com.example.moviecataloguefour.db.DatabaseContract.FavoriteColums.Companion.TABLE_MOVIE
import com.example.moviecataloguefour.db.DatabaseContract.FavoriteColums.Companion.TITLE
import com.example.moviecataloguefour.db.DatabaseContract.FavoriteColums.Companion._ID
import com.example.moviecataloguefour.db.DatabaseContract.FavoriteColumsTv.Companion.TABLE_TV

internal class DatabaseHelper(context: Context?): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "dbfavorite"
        private const val DATABASE_VERSION = 1

        private val SQL_CREATE_TABLE_FAVORITE_MOVIE = "CREATE TABLE $TABLE_MOVIE" +
                "(${_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${TITLE} TEXT NOT NULL," +
                "${DESCRIPTION} TEXT NOT NULL," +
                "${POSTER} TEXT NOT NULL)"

        private val SQL_CREATE_TABLE_FAVORITE_TV = "CREATE TABLE $TABLE_TV" +
                "(${_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${TITLE} TEXT NOT NULL," +
                "${DESCRIPTION} TEXT NOT NULL," +
                "${POSTER} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE_MOVIE)
        db.execSQL(SQL_CREATE_TABLE_FAVORITE_TV)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MOVIE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TV")
        onCreate(db)
    }
}