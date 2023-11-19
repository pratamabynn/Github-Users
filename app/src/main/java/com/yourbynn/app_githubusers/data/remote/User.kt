package com.yourbynn.app_githubusers.data.remote

import com.google.gson.annotations.SerializedName

data class User(

    @SerializedName("id")
    val id: Int,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("html_url")
    val htmlUrl: String,
    @SerializedName("login")
    val login: String
)