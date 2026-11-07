package com.pes.githubexplorer

import com.google.gson.annotations.SerializedName

data class GitHubRepository(
    val id: Long,
    val name: String,

    @SerializedName("full_name")
    val fullName: String,

    @SerializedName("html_url")
    val htmlUrl: String,

    val description: String? = null,
    val language: String? = null,

    @SerializedName("stargazers_count")
    val stars: Int = 0,

    @SerializedName("forks_count")
    val forks: Int = 0
)