package com.example.githubuserslist.Helper

import android.database.Cursor
import com.example.githubuserslist.db.DatabaseContract
import com.example.githubuserslist.entity.FavoriteItems

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<FavoriteItems> {
        val favoritesList = ArrayList<FavoriteItems>()
        notesCursor?.apply {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.NAME))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.USERNAME))
                val profile_picture = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.PROFILE_PICTURE))
                val following = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.FOLLOWING))
                val followers = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.FOLLOWERS))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.LOCATION))

                favoritesList.add(FavoriteItems(name, username, profile_picture, followers, following, location))
            }
        }
        return favoritesList
    }

    fun mapCursorToObject(favoritCursor: Cursor?): FavoriteItems {
        var favoriteItems = FavoriteItems()
        favoritCursor?.apply {
            moveToFirst()
            val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.NAME))
            val name = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.USERNAME))
            val profile_picture = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.PROFILE_PICTURE))
            val followers = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.FOLLOWERS))
            val following = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.FOLLOWING))
            val location = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.LOCATION))
            favoriteItems = FavoriteItems(name, username, profile_picture, followers, following, location)
        }
        return favoriteItems
    }

}