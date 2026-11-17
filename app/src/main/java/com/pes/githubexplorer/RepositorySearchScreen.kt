package com.pes.githubexplorer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositorySearchScreen(
    onRepositoryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    searchViewModel: RepositorySearchViewModel = viewModel()
) {
    val state = searchViewModel.uiState

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.repo_search_title),
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = searchViewModel.query,
            onValueChange = searchViewModel::updateQuery,
            label = { Text(stringResource(R.string.repo_search_hint)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { searchViewModel.search() }
            )
        )

        Button(
            onClick = { searchViewModel.search() },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.user_search_button))
        }

        // Sort options
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RepositorySort.values().forEach { option ->
                FilterChip(
                    selected = searchViewModel.sort == option,
                    onClick = { searchViewModel.updateSort(option) },
                    label = { Text(stringResource(option.labelResId)) }
                )
            }
        }

        if (state.hasSearched && state.repositories.isNotEmpty()) {
            Text(
                text = stringResource(
                    R.string.repo_search_count,
                    state.totalCount
                ),
                style = MaterialTheme.typography.bodySmall
            )
        }

        when {
            state.repositories.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = state.repositories,
                        key = { it.id }
                    ) { repository ->
                        RepositoryResultCard(
                            repository = repository,
                            onClick = { onRepositoryClick(repository.fullName) }
                        )
                    }

                    // Footer: loading / error / load more
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            when {
                                state.isLoading -> CircularProgressIndicator()

                                state.errorResId != null -> {
                                    Column(
                                        horizontalAlignment =
                                            Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = stringResource(
                                                state.errorResId
                                            ),
                                            color = MaterialTheme
                                                .colorScheme.error
                                        )
                                        TextButton(
                                            onClick = { searchViewModel.retry() }
                                        ) {
                                            Text(
                                                stringResource(
                                                    R.string.user_retry
                                                )
                                            )
                                        }
                                    }
                                }

                                state.hasMore -> {
                                    Button(
                                        onClick = { searchViewModel.loadMore() }
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
                }
            }

            state.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(stringResource(R.string.repo_search_loading))
            }

            state.errorResId != null -> {
                Text(
                    text = stringResource(state.errorResId),
                    color = MaterialTheme.colorScheme.error
                )
                TextButton(onClick = { searchViewModel.retry() }) {
                    Text(stringResource(R.string.user_retry))
                }
            }

            !state.hasSearched -> {
                Text(stringResource(R.string.repo_search_intro))
            }

            else -> {
                Text(stringResource(R.string.repo_search_empty))
            }
        }
    }
}

@Composable
private fun RepositoryResultCard(
    repository: GitHubRepository,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Owner avatar loaded with Glide (reuses teammate's composable)
            repository.owner?.let { owner ->
                UserAvatar(
                    url = owner.avatarUrl,
                    username = owner.login
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = repository.fullName,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = repository.description
                        ?.takeIf { it.isNotBlank() }
                        ?: stringResource(
                            R.string.user_repository_no_description
                        ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = repository.language
                        ?: stringResource(
                            R.string.user_repository_no_language
                        ),
                    style = MaterialTheme.typography.bodySmall
                )

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = stringResource(
                            R.string.user_repository_stars,
                            repository.stars
                        ),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = stringResource(
                            R.string.user_repository_forks,
                            repository.forks
                        ),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
