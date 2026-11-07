package com.pes.githubexplorer

import com.google.gson.annotations.SerializedName

// Response returned when searching users
data class UserSearchResponse(
    @SerializedName("total_count")
    val totalCount: Int = 0,

    val items: List<GitHubUser> = emptyList()
)

// Basic user information in search results
data class GitHubUser(
    val id: Long,
    val login: String,

    @SerializedName("avatar_url")
    val avatarUrl: String,

    @SerializedName("html_url")
    val htmlUrl: String
)

// Full information shown on the profile screen
data class GitHubUserProfile(
    val login: String,

    @SerializedName("avatar_url")
    val avatarUrl: String,

    @SerializedName("html_url")
    val htmlUrl: String,

    val name: String? = null,
    val bio: String? = null,
    val location: String? = null,

    @SerializedName("public_repos")
    val publicRepos: Int = 0,

    val followers: Int = 0,
    val following: Int = 0
)