package com.pes.githubexplorer

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {

    @Headers("Accept: application/vnd.github+json")
    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20
    ): UserSearchResponse

    @Headers("Accept: application/vnd.github+json")
    @GET("users/{username}")
    suspend fun getUserProfile(
        @Path("username") username: String
    ): GitHubUserProfile


    @Headers("Accept: application/vnd.github+json")
    @GET("users/{username}/repos")
    suspend fun getUserRepositories(
        @Path("username") username: String,
        @Query("sort") sort: String = "updated",
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 1
    ): List<GitHubRepository>
}