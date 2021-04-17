package com.example.githubuserslist.Provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.media.tv.TvContract.AUTHORITY
import android.net.Uri
import com.example.githubuserslist.Helper.FavoriteUserHelper
import com.example.githubuserslist.db.DatabaseContract.FavoriteUserColumns.Companion.CONTENT_URI
import com.example.githubuserslist.db.DatabaseContract.FavoriteUserColumns.Companion.TABLE_NAME

class FavoriteProvider : ContentProvider() {

    companion object {
        private const val FAVORIT = 1
        private const val FAVORIT_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favoriteUserHelper: FavoriteUserHelper
        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVORIT)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", FAVORIT_ID)
        }
    }

    override fun onCreate(): Boolean {
        favoriteUserHelper = FavoriteUserHelper.getInstance(context as Context)
        favoriteUserHelper.open()
        return true
    }
    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return when (sUriMatcher.match(uri)) {
            FAVORIT -> favoriteUserHelper.queryAll()
            FAVORIT_ID -> favoriteUserHelper.queryByUsername(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (FAVORIT) {
            sUriMatcher.match(uri) -> {
                favoriteUserHelper.insert(values)
            }
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val updated: Int = when (FAVORIT_ID) {
            sUriMatcher.match(uri) -> favoriteUserHelper.update(uri.lastPathSegment.toString(),values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (FAVORIT_ID) {
            sUriMatcher.match(uri) -> favoriteUserHelper.deleteByUsername(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }
}
