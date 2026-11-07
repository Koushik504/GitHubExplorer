package com.pes.githubexplorer

import android.widget.ImageView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.Glide

@Composable
fun UserSearchScreen(
    onUserClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    searchViewModel: UserSearchViewModel = viewModel()
) {
    val state = searchViewModel.uiState

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.user_search_title),
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = searchViewModel.query,
            onValueChange = searchViewModel::updateQuery,
            label = {
                Text(stringResource(R.string.user_search_hint))
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
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

        when {
            state.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )
                )
                Text(stringResource(R.string.user_search_loading))
            }

            state.errorResId != null -> {
                Text(
                    text = stringResource(state.errorResId),
                    color = MaterialTheme.colorScheme.error
                )
                TextButton(
                    onClick = { searchViewModel.search() }
                ) {
                    Text(stringResource(R.string.user_retry))
                }
            }

            !state.hasSearched -> {
                Text(stringResource(R.string.user_search_intro))
            }

            state.users.isEmpty() -> {
                Text(stringResource(R.string.user_search_empty))
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = state.users,
                        key = { it.id }
                    ) { user ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onUserClick(user.login)
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment =
                                    Alignment.CenterVertically,
                                horizontalArrangement =
                                    Arrangement.spacedBy(12.dp)
                            ) {
                                UserAvatar(
                                    url = user.avatarUrl,
                                    username = user.login
                                )

                                Text(
                                    text = user.login,
                                    style = MaterialTheme
                                        .typography.titleMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserAvatar(
    url: String,
    username: String,
    modifier: Modifier = Modifier
) {
    val description = stringResource(
        R.string.user_avatar_description,
        username
    )

    AndroidView(
        modifier = modifier.size(56.dp),
        factory = { context ->
            ImageView(context).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        },
        update = { imageView ->
            imageView.contentDescription = description

            Glide.with(imageView)
                .load(url)
                .circleCrop()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(imageView)
        },
        onRelease = { imageView ->
            Glide.with(imageView).clear(imageView)
        }
    )
}