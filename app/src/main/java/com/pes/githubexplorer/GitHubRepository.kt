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
    val forks: Int = 0,

    // ---- Added for Repository Search & Details (all optional) ----
    val owner: RepositoryOwner? = null,

    @SerializedName("open_issues_count")
    val openIssues: Int = 0,

    // Only returned by the single-repository endpoint
    @SerializedName("subscribers_count")
    val watchers: Int = 0,

    @SerializedName("default_branch")
    val defaultBranch: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null,

    val license: RepositoryLicense? = null,
    val topics: List<String>? = null
)

data class RepositoryOwner(
    val login: String,

    @SerializedName("avatar_url")
    val avatarUrl: String
)

data class RepositoryLicense(
    val name: String? = null
)

// Response returned when searching repositories
data class RepositorySearchResponse(
    @SerializedName("total_count")
    val totalCount: Int = 0,

    val items: List<GitHubRepository> = emptyList()
)
