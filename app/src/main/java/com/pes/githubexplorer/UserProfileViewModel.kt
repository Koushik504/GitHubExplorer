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

data class UserProfileUiState(
    val profile: GitHubUserProfile? = null,
    val isLoading: Boolean = false,
    val errorResId: Int? = null
)

class UserProfileViewModel : ViewModel() {

    var uiState by mutableStateOf(UserProfileUiState())
        private set

    private var profileJob: Job? = null
    private var loadedUsername: String? = null

    fun loadProfile(username: String, force: Boolean = false) {
        if (!force && loadedUsername == username &&
            (uiState.profile != null || uiState.isLoading)
        ) {
            return
        }

        profileJob?.cancel()
        loadedUsername = username

        profileJob = viewModelScope.launch {
            uiState = UserProfileUiState(isLoading = true)

            try {
                val profile = RetrofitClient.api
                    .getUserProfile(username)

                uiState = UserProfileUiState(profile = profile)
            } catch (e: CancellationException) {
                throw e
            } catch (e: HttpException) {
                uiState = UserProfileUiState(
                    errorResId = if (e.code() == 403 || e.code() == 429) {
                        R.string.user_rate_limit_error
                    } else {
                        R.string.user_generic_error
                    }
                )
            } catch (e: IOException) {
                uiState = UserProfileUiState(
                    errorResId = R.string.user_network_error
                )
            } catch (e: Exception) {
                uiState = UserProfileUiState(
                    errorResId = R.string.user_generic_error
                )
            }
        }
    }
}