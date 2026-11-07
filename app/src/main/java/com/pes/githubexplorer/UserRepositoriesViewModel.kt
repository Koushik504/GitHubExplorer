package com.pes.githubexplorer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException

data class UserRepositoriesUiState(
    val repositories: List<GitHubRepository> = emptyList(),
    val isLoading: Boolean = false,
    val errorResId: Int? = null,
    val hasMore: Boolean = true
)

class UserRepositoriesViewModel : ViewModel() {

    var uiState by mutableStateOf(UserRepositoriesUiState())
        private set

    private var currentUsername: String? = null
    private var nextPage = 1
    private var loadJob: Job? = null

    fun loadRepositories(username: String) {
        if (currentUsername == username) return

        loadJob?.cancel()
        currentUsername = username
        nextPage = 1
        uiState = UserRepositoriesUiState()

        loadNextPage()
    }

    fun loadNextPage() {
        val username = currentUsername ?: return

        if (uiState.isLoading || !uiState.hasMore) return

        loadJob = viewModelScope.launch {
            uiState = uiState.copy(
                isLoading = true,
                errorResId = null
            )

            try {
                val repositories =
                    RetrofitClient.api.getUserRepositories(
                        username = username,
                        page = nextPage,
                        perPage = 20
                    )

                uiState = uiState.copy(
                    repositories = (
                            uiState.repositories + repositories
                            ).distinctBy { it.id },
                    isLoading = false,
                    hasMore = repositories.size == 20
                )

                nextPage++
            } catch (e: CancellationException) {
                throw e
            } catch (e: HttpException) {
                showError(
                    if (e.code() == 403 || e.code() == 429) {
                        R.string.user_rate_limit_error
                    } else {
                        R.string.user_generic_error
                    }
                )
            } catch (e: IOException) {
                showError(R.string.user_network_error)
            } catch (e: Exception) {
                showError(R.string.user_generic_error)
            }
        }
    }

    private fun showError(messageResId: Int) {
        uiState = uiState.copy(
            isLoading = false,
            errorResId = messageResId
        )
    }
}