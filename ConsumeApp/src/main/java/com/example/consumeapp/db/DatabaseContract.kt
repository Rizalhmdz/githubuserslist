package com.example.consumeapp.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.example.githubuserslist"
    const val SCHEME = "content"

    class FavoriteUserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite_user"
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