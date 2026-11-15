package com.pes.githubexplorer.favorites

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pes.githubexplorer.GitHubRepository

/**
 * Persists the user's favorite repositories locally.
 * Uses the existing GitHubRepository model from the main project.
 */
class FavoritesStorage(context: Context) {
    private val preferences = context.applicationContext
        .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getFavorites(): List<GitHubRepository> {
        val json = preferences.getString(KEY_FAVORITES, null) ?: return emptyList()
        return runCatching {
            val type = object : TypeToken<List<GitHubRepository>>() {}.type
            gson.fromJson<List<GitHubRepository>>(json, type) ?: emptyList()
        }.getOrDefault(emptyList())
    }

    fun saveFavorites(favorites: List<GitHubRepository>) {
        preferences.edit()
            .putString(KEY_FAVORITES, gson.toJson(favorites))
            .apply()
    }

    fun clearFavorites() {
        preferences.edit().remove(KEY_FAVORITES).apply()
    }

    private companion object {
        const val PREFERENCES_NAME = "github_explorer_favorites"
        const val KEY_FAVORITES = "favorite_repositories"
    }
}
