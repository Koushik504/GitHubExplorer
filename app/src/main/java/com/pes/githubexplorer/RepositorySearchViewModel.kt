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

enum class RepositorySort(val apiValue: String?, val labelResId: Int) {
    BEST_MATCH(null, R.string.repo_sort_best),
    STARS("stars", R.string.repo_sort_stars),
    FORKS("forks", R.string.repo_sort_forks),
    UPDATED("updated", R.string.repo_sort_updated)
}

data class RepositorySearchUiState(
    val repositories: List<GitHubRepository> = emptyList(),
    val totalCount: Int = 0,
    val isLoading: Boolean = false,
    val hasSearched: Boolean = false,
    val hasMore: Boolean = false,
    val errorResId: Int? = null
)

class RepositorySearchViewModel : ViewModel() {

    var query by mutableStateOf("")
        private set

    var sort by mutableStateOf(RepositorySort.BEST_MATCH)
        private set

    var uiState by mutableStateOf(RepositorySearchUiState())
        private set

    private var activeQuery = ""
    private var nextPage = 1
    private var searchJob: Job? = null

    fun updateQuery(value: String) {
        query = value
    }

    fun updateSort(value: RepositorySort) {
        sort = value
        // Re-run the current search with the new ordering
        if (uiState.hasSearched && query.isNotBlank()) {
            search()
        }
    }

    fun search() {
        val searchText = query.trim()

        searchJob?.cancel()

        if (searchText.isBlank()) {
            uiState = RepositorySearchUiState(
                errorResId = R.string.repo_search_blank
            )
            return
        }

        activeQuery = searchText
        nextPage = 1
        uiState = RepositorySearchUiState(isLoading = true)
        fetch(reset = true)
    }

    fun loadMore() {
        if (uiState.isLoading || !uiState.hasMore) return
        fetch(reset = false)
    }

    // Retry after an error: restart if nothing is loaded, else load next page
    fun retry() {
        if (uiState.repositories.isEmpty()) search() else loadMore()
    }

    private fun fetch(reset: Boolean) {
        searchJob = viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorResId = null)

            try {
                val response = RetrofitClient.api.searchRepositories(
                    query = activeQuery,
                    sort = sort.apiValue,
                    page = nextPage,
                    perPage = PAGE_SIZE
                )

                val all = if (reset) {
                    response.items
                } else {
                    (uiState.repositories + response.items).distinctBy { it.id }
                }

                nextPage++

                uiState = RepositorySearchUiState(
                    repositories = all,
                    totalCount = response.totalCount,
                    hasSearched = true,
                    // GitHub only serves the first 1000 search results
                    hasMore = response.items.size == PAGE_SIZE &&
                            all.size < minOf(response.totalCount, MAX_RESULTS)
                )
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
            hasSearched = true,
            errorResId = messageResId
        )
    }

    private companion object {
        const val PAGE_SIZE = 20
        const val MAX_RESULTS = 1000
    }
}
