package com.example.githubuserslist

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.githubuserslist.db.DatabaseContract
import com.example.githubuserslist.db.DatabaseContract.FavoriteUserColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "db_githubuserslist"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.FavoriteUserColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.FavoriteUserColumns.NAME} String NOT NULL," +
                " ${DatabaseContract.FavoriteUserColumns.USERNAME} String NOT NULL," +
                " ${DatabaseContract.FavoriteUserColumns.PROFILE_PICTURE} String NOT NULL,)" +
                " ${DatabaseContract.FavoriteUserColumns.FOLLOWERS} String NOT NULL," +
                " ${DatabaseContract.FavoriteUserColumns.FOLLOWING} String NOT NULL," +
                " ${DatabaseContract.FavoriteUserColumns.LOCATION} String NOT NULL"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}