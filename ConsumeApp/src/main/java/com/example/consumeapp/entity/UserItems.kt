package com.example.consumeapp.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserItems(
    var name: String? = "",
    var username: String? = "",
    var profile_picture: String? = "",
    var followers: String? = "",
    var following: String? = "",
    var location: String?  = ""
) : Parcelable
