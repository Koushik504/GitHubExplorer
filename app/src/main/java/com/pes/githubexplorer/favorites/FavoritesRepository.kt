package com.pes.githubexplorer.favorites

import android.content.Context
import com.pes.githubexplorer.GitHubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** Business logic for adding, removing and reading favorite repositories. */
class FavoritesRepository(context: Context) {
    private val favoriteDao = FavoritesDatabase.getInstance(context).favoriteDao()

    val favorites: Flow<List<GitHubRepository>> = favoriteDao.observeAll()
        .map { favorites -> favorites.map(FavoriteEntity::toGitHubRepository) }

    fun observeIsFavorite(repositoryId: Long): Flow<Boolean> =
        favoriteDao.observeIsFavorite(repositoryId)

    suspend fun addFavorite(repository: GitHubRepository) =
        favoriteDao.insert(FavoriteEntity.from(repository))

    suspend fun removeFavorite(repositoryId: Long) =
        favoriteDao.deleteById(repositoryId)

    suspend fun clearFavorites() = favoriteDao.clearAll()
}
