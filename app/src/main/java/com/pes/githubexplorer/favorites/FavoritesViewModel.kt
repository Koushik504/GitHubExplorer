package com.pes.githubexplorer.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pes.githubexplorer.GitHubRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** State holder for the Favorites screen. */
class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FavoritesRepository(application)

    private val _favorites = MutableStateFlow(repository.getFavorites())
    val favorites: StateFlow<List<GitHubRepository>> = _favorites.asStateFlow()

    fun refresh() {
        _favorites.value = repository.getFavorites()
    }

    fun addFavorite(item: GitHubRepository) {
        viewModelScope.launch {
            repository.addFavorite(item)
            refresh()
        }
    }

    fun removeFavorite(repositoryId: Long) {
        viewModelScope.launch {
            repository.removeFavorite(repositoryId)
            refresh()
        }
    }

    fun clearFavorites() {
        viewModelScope.launch {
            repository.clearFavorites()
            refresh()
        }
    }

    fun isFavorite(repositoryId: Long): Boolean =
        repository.isFavorite(repositoryId)
}
