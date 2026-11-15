package com.pes.githubexplorer.favorites

import android.content.Context
import com.pes.githubexplorer.GitHubRepository

/** Business logic for adding, removing and reading favorite repositories. */
class FavoritesRepository(context: Context) {
    private val storage = FavoritesStorage(context)

    fun getFavorites(): List<GitHubRepository> = storage.getFavorites()

    fun addFavorite(repository: GitHubRepository) {
        val current = storage.getFavorites()
        if (current.none { it.id == repository.id }) {
            storage.saveFavorites(current + repository)
        }
    }

    fun removeFavorite(repositoryId: Long) {
        storage.saveFavorites(storage.getFavorites().filterNot { it.id == repositoryId })
    }

    fun isFavorite(repositoryId: Long): Boolean =
        storage.getFavorites().any { it.id == repositoryId }

    fun clearFavorites() = storage.clearFavorites()
}
