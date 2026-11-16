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

data class RepositoryDetailsUiState(
    val repository: GitHubRepository? = null,
    val isLoading: Boolean = false,
    val errorResId: Int? = null
)

class RepositoryDetailsViewModel : ViewModel() {

    var uiState by mutableStateOf(RepositoryDetailsUiState())
        private set

    private var loadedFullName: String? = null
    private var loadJob: Job? = null

    // fullName looks like "owner/repo"
    fun load(fullName: String, force: Boolean = false) {
        if (!force &&
            loadedFullName == fullName &&
            (uiState.isLoading || uiState.repository != null)
        ) return

        val parts = fullName.split("/", limit = 2)
        if (parts.size < 2) {
            uiState = RepositoryDetailsUiState(
                errorResId = R.string.user_generic_error
            )
            return
        }

        loadJob?.cancel()
        loadedFullName = fullName

        loadJob = viewModelScope.launch {
            uiState = RepositoryDetailsUiState(isLoading = true)

            try {
                val repository = RetrofitClient.api.getRepository(
                    owner = parts[0],
                    repo = parts[1]
                )
                uiState = RepositoryDetailsUiState(repository = repository)
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
        uiState = RepositoryDetailsUiState(errorResId = messageResId)
    }
}
