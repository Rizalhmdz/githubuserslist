package com.example.consumeapp.Helper

import android.database.Cursor
import com.example.consumeapp.db.DatabaseContract
import com.example.consumeapp.entity.FavoriteItems

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
}