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

data class UserSearchUiState(
    val users: List<GitHubUser> = emptyList(),
    val isLoading: Boolean = false,
    val hasSearched: Boolean = false,
    val errorResId: Int? = null
)

class UserSearchViewModel : ViewModel() {

    var query by mutableStateOf("")
        private set

    var uiState by mutableStateOf(UserSearchUiState())
        private set

    private var searchJob: Job? = null

    fun updateQuery(value: String) {
        query = value
    }

    fun search() {
        val searchText = query.trim()

        searchJob?.cancel()

        if (searchText.isBlank()) {
            uiState = UserSearchUiState(
                errorResId = R.string.user_search_blank
            )
            return
        }

        searchJob = viewModelScope.launch {
            uiState = UserSearchUiState(isLoading = true)

            try {
                val response = RetrofitClient.api.searchUsers(
                    query = searchText
                )

                uiState = UserSearchUiState(
                    users = response.items,
                    hasSearched = true
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: HttpException) {
                uiState = UserSearchUiState(
                    hasSearched = true,
                    errorResId = if (e.code() == 403 || e.code() == 429) {
                        R.string.user_rate_limit_error
                    } else {
                        R.string.user_generic_error
                    }
                )
            } catch (e: IOException) {
                uiState = UserSearchUiState(
                    hasSearched = true,
                    errorResId = R.string.user_network_error
                )
            } catch (e: Exception) {
                uiState = UserSearchUiState(
                    hasSearched = true,
                    errorResId = R.string.user_generic_error
                )
            }
        }
    }
}