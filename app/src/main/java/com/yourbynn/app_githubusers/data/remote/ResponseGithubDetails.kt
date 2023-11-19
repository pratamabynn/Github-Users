package com.yourbynn.app_githubusers.data.remote

import com.google.gson.annotations.SerializedName

data class ResponseGithubDetails (
    @SerializedName("total_count")
    val totalCount: Int?,
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean?,
    @SerializedName("items")
    val users: List<User?>?
)