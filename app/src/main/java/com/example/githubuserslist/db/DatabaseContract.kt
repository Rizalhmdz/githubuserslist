package com.example.githubuserslist.db

import android.os.Parcelable
import android.provider.BaseColumns
import kotlinx.android.parcel.Parcelize

class DatabaseContract {
    internal class FavoriteUserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite_user"
            const val _ID = "_id"
            const val NAME = "name"
            const val USERNAME = "username"
            const val PROFILE_PICTURE = "profile_picture"
            const val FOLLOWERS = "followers"
            const val FOLLOWING = "following"
            const val LOCATION = "location"
        }
    }
}