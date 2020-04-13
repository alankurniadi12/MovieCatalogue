package com.example.moviecataloguefour.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.moviecataloguefour.db.DatabaseContract.FavoriteColums.Companion.TABLE_MOVIE
import com.example.moviecataloguefour.db.DatabaseContract.FavoriteColums.Companion._ID
import java.sql.SQLException

class MovieHelper(context: Context?) {

    companion object {
        private const val DATABASE_TABLE = TABLE_MOVIE
        private var INSTANCE: MovieHelper? = null
        private lateinit var databaseHelper: DatabaseHelper
        private lateinit var database: SQLiteDatabase

        fun getInstance(context: Context?): MovieHelper =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: MovieHelper(context)
        }
    }

    init {
        databaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE, null, null, null, null, null, "$_ID ASC"
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE, null, "$_ID = ?", arrayOf(id), null, null, null, null
        )
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }
}