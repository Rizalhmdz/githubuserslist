package com.example.githubuserslist.Helper

import android.database.Cursor
import com.example.githubuserslist.db.DatabaseContract
import com.example.githubuserslist.entity.FavoriteItems

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<FavoriteItems> {
        val favoritesList = ArrayList<FavoriteItems>()
        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns._ID))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.NAME))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.USERNAME))
                val profile_picture = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.PROFILE_PICTURE))
                val following = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.FOLLOWING))
                val followers = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.FOLLOWERS))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.LOCATION))

                favoritesList.add(FavoriteItems(id, name, username, profile_picture, followers, following, location))
            }
        }
        return favoritesList
    }
}