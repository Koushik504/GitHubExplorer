package com.pes.githubexplorer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun UserRepositoriesSection(
    username: String,
    repositoriesViewModel: UserRepositoriesViewModel = viewModel()
) {
    val state = repositoriesViewModel.uiState

    LaunchedEffect(username) {
        repositoriesViewModel.loadRepositories(username)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HorizontalDivider()

        Text(
            text = stringResource(R.string.user_repositories_title),
            style = MaterialTheme.typography.titleLarge
        )

        state.repositories.forEach { repository ->
            UserRepositoryCard(repository = repository)
        }

        when {
            state.isLoading -> {
                CircularProgressIndicator()
                Text(
                    stringResource(
                        R.string.user_repositories_loading
                    )
                )
            }

            state.errorResId != null -> {
                Text(
                    text = stringResource(state.errorResId),
                    color = MaterialTheme.colorScheme.error
                )

                TextButton(
                    onClick = {
                        repositoriesViewModel.loadNextPage()
                    }
                ) {
                    Text(stringResource(R.string.user_retry))
                }
            }

            state.repositories.isEmpty() && !state.hasMore -> {
                Text(
                    stringResource(
                        R.string.user_repositories_empty
                    )
                )
            }

            state.repositories.isNotEmpty() && state.hasMore -> {
                Button(
                    onClick = {
                        repositoriesViewModel.loadNextPage()
                    }
                ) {
                    Text(
                        stringResource(
                            R.string.user_repositories_load_more
                        )
                    )
                }
            }
        }
    }
}