package com.yourbynn.app_githubusers.config

import com.yourbynn.app_githubusers.data.remote.ResponseGithubDetails
import com.yourbynn.app_githubusers.data.remote.ResponseUserDetails
import com.yourbynn.app_githubusers.data.remote.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getUsers(
        @Query("q") query: String
    ): Call<ResponseGithubDetails>

    @GET("users/{username}")
    fun getUserDetails(
        @Path("username") username: String?
    ): Call<ResponseUserDetails>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String?
    ): Call<List<User>>

    @GET("users/{username}/following")
    fun getUserFollowings(
        @Path("username") username: String?
    ): Call<List<User>>
}