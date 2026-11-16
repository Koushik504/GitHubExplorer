package com.pes.githubexplorer

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pes.githubexplorer.favorites.FavoritesRepository
import kotlinx.coroutines.launch

@Composable
fun RepositoryDetailsScreen(
    fullName: String,
    onBack: () -> Unit,
    onOwnerClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    detailsViewModel: RepositoryDetailsViewModel = viewModel()
) {
    val state = detailsViewModel.uiState

    LaunchedEffect(fullName) {
        detailsViewModel.load(fullName)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextButton(onClick = onBack) {
            Text(stringResource(R.string.user_back))
        }

        Text(
            text = stringResource(R.string.repo_details_title),
            style = MaterialTheme.typography.headlineSmall
        )

        when {
            state.isLoading -> {
                CircularProgressIndicator()
                Text(stringResource(R.string.repo_details_loading))
            }

            state.errorResId != null -> {
                Text(
                    text = stringResource(state.errorResId),
                    color = MaterialTheme.colorScheme.error
                )
                Button(
                    onClick = {
                        detailsViewModel.load(fullName, force = true)
                    }
                ) {
                    Text(stringResource(R.string.user_retry))
                }
            }

            state.repository != null -> {
                RepositoryDetailsContent(
                    repository = state.repository,
                    onOwnerClick = onOwnerClick
                )
            }
        }
    }
}

@Composable
private fun RepositoryDetailsContent(
    repository: GitHubRepository,
    onOwnerClick: (String) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val favoritesRepository = remember { FavoritesRepository(context) }
    val isFavorite by favoritesRepository
        .observeIsFavorite(repository.id)
        .collectAsState(initial = false)

    val shareTitle = stringResource(R.string.user_repository_share_title)
    val noAppMessage = stringResource(R.string.user_no_app_available)

    // Owner
    repository.owner?.let { owner ->
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            UserAvatar(url = owner.avatarUrl, username = owner.login)
            Text(
                text = "@${owner.login}",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

    Text(
        text = repository.fullName,
        style = MaterialTheme.typography.headlineMedium
    )

    Text(
        text = repository.description
            ?.takeIf { it.isNotBlank() }
            ?: stringResource(R.string.user_repository_no_description)
    )

    repository.topics?.takeIf { it.isNotEmpty() }?.let { topics ->
        Text(
            text = stringResource(
                R.string.repo_details_topics,
                topics.joinToString(", ")
            ),
            style = MaterialTheme.typography.bodySmall
        )
    }

    HorizontalDivider()

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                stringResource(
                    R.string.repo_details_language,
                    repository.language
                        ?: stringResource(R.string.user_repository_no_language)
                )
            )
            Text(stringResource(R.string.user_repository_stars, repository.stars))
            Text(stringResource(R.string.user_repository_forks, repository.forks))
            Text(stringResource(R.string.repo_details_watchers, repository.watchers))
            Text(
                stringResource(R.string.repo_details_issues, repository.openIssues)
            )
            Text(
                stringResource(
                    R.string.repo_details_license,
                    repository.license?.name
                        ?: stringResource(R.string.repo_details_no_license)
                )
            )
            Text(
                stringResource(
                    R.string.repo_details_branch,
                    repository.defaultBranch ?: "-"
                )
            )
            Text(
                stringResource(
                    R.string.repo_details_updated,
                    repository.updatedAt?.take(10) ?: "-"
                )
            )
        }
    }

    // SAVE / REMOVE FAVORITE (stored with Room)
    OutlinedButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            coroutineScope.launch {
                if (isFavorite) {
                    favoritesRepository.removeFavorite(repository.id)
                } else {
                    favoritesRepository.addFavorite(repository)
                }
            }
        }
    ) {
        Text(
            stringResource(
                if (isFavorite) R.string.repo_remove_favorite
                else R.string.repo_add_favorite
            )
        )
    }

    // IMPLICIT INTENT 1: open in browser
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repository.htmlUrl))
            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, noAppMessage, Toast.LENGTH_SHORT).show()
            }
        }
    ) {
        Text(stringResource(R.string.user_open_browser))
    }

    // IMPLICIT INTENT 2: share link
    OutlinedButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, repository.htmlUrl)
            }
            try {
                context.startActivity(Intent.createChooser(intent, shareTitle))
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, noAppMessage, Toast.LENGTH_SHORT).show()
            }
        }
    ) {
        Text(stringResource(R.string.user_repository_share))
    }

    repository.owner?.let { owner ->
        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onOwnerClick(owner.login) }
        ) {
            Text(stringResource(R.string.repo_view_owner))
        }
    }
}
