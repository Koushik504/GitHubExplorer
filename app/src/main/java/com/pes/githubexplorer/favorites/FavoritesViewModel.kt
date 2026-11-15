package com.pes.githubexplorer.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pes.githubexplorer.GitHubRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** State holder for the Favorites screen. */
class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FavoritesRepository(application)

    val favorites: StateFlow<List<GitHubRepository>> = repository.favorites.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun addFavorite(item: GitHubRepository) {
        viewModelScope.launch {
            repository.addFavorite(item)
        }
    }

    fun removeFavorite(repositoryId: Long) {
        viewModelScope.launch {
            repository.removeFavorite(repositoryId)
        }
    }

    fun clearFavorites() {
        viewModelScope.launch {
            repository.clearFavorites()
        }
    }

}
