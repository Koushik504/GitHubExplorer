package com.pes.githubexplorer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun UserProfileScreen(
    username: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    profileViewModel: UserProfileViewModel = viewModel()
) {
    val state = profileViewModel.uiState
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(username) {
        profileViewModel.loadProfile(username)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = onBack) {
            Text(stringResource(R.string.user_back))
        }

        Text(
            text = stringResource(R.string.user_profile_title),
            style = MaterialTheme.typography.headlineSmall
        )

        when {
            state.isLoading -> {
                CircularProgressIndicator()
                Text(
                    stringResource(R.string.user_profile_loading)
                )
            }

            state.errorResId != null -> {
                Text(
                    text = stringResource(state.errorResId),
                    color = MaterialTheme.colorScheme.error
                )

                Button(
                    onClick = {
                        profileViewModel.loadProfile(
                            username = username,
                            force = true
                        )
                    }
                ) {
                    Text(stringResource(R.string.user_retry))
                }
            }

            else -> {
                state.profile?.let { profile ->
                    UserAvatar(
                        url = profile.avatarUrl,
                        username = profile.login
                    )

                    Text(
                        text = profile.name
                            ?.takeIf { it.isNotBlank() }
                            ?: profile.login,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Text(
                        text = "@${profile.login}",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = profile.bio
                            ?.takeIf { it.isNotBlank() }
                            ?: stringResource(
                                R.string.user_profile_no_bio
                            )
                    )

                    Text(
                        text = profile.location
                            ?.takeIf { it.isNotBlank() }
                            ?: stringResource(
                                R.string.user_profile_no_location
                            )
                    )

                    HorizontalDivider()

                    Text(
                        stringResource(
                            R.string.user_profile_repos,
                            profile.publicRepos
                        )
                    )

                    Text(
                        stringResource(
                            R.string.user_profile_followers,
                            profile.followers
                        )
                    )

                    Text(
                        stringResource(
                            R.string.user_profile_following,
                            profile.following
                        )
                    )
                    UserRepositoriesSection(
                        username = profile.login
                    )

                    Button(
                        onClick = {
                            uriHandler.openUri(profile.htmlUrl)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            stringResource(
                                R.string.user_open_browser
                            )
                        )
                    }
                }
            }
        }
    }
}