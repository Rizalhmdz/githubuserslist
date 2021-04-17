package com.example.githubuserslist.db

import android.media.tv.TvContract.AUTHORITY
import android.net.Uri
import android.os.Parcelable
import android.provider.BaseColumns
import android.service.notification.Condition.SCHEME
import com.example.githubuserslist.db.DatabaseContract.FavoriteUserColumns.Companion.TABLE_NAME
import kotlinx.android.parcel.Parcelize

class DatabaseContract {

    val AUTHORITY = "com.example.githubuserslist"
    val SCHEME = "content"

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

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}