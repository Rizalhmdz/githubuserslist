package com.example.githubuserslist.Helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.githubuserslist.db.DatabaseContract.FavoriteUserColumns.Companion.TABLE_NAME
import com.example.githubuserslist.db.DatabaseContract.FavoriteUserColumns.Companion.USERNAME
import com.example.githubuserslist.db.DatabaseContract.FavoriteUserColumns.Companion._ID
import java.sql.SQLException

class FavoriteUserHelper(context: Context) {

    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private var database: SQLiteDatabase = dataBaseHelper.writableDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: FavoriteUserHelper? = null
        fun getInstance(context: Context): FavoriteUserHelper =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: FavoriteUserHelper(context)
                }

    }

    init {
        dataBaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                "$USERNAME ASC"
        )
    }

    fun queryByUsername(id: String): Cursor {
        return database.query(
                DATABASE_TABLE,
                null,
                "$USERNAME = ?",
                arrayOf(id),
                null,
                null,
                null,
                null)
    }


    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }

    fun deleteByUsername(id: String): Int {
        return database.delete(DATABASE_TABLE, "$USERNAME = '$id'", null)
    }

}