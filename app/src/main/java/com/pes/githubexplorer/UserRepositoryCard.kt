
package com.pes.githubexplorer

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pes.githubexplorer.favorites.FavoritesRepository

@Composable
fun UserRepositoryCard(
    repository: GitHubRepository,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Connect to your Favorites storage
    val favoritesRepository = remember {
        FavoritesRepository(context)
    }

    // Check whether this repository is already saved
    var isFavorite by remember(repository.id) {
        mutableStateOf(
            favoritesRepository.isFavorite(repository.id)
        )
    }

    val shareTitle = stringResource(
        R.string.user_repository_share_title
    )

    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = repository.fullName,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = repository.description
                    ?.takeIf { it.isNotBlank() }
                    ?: stringResource(
                        R.string.user_repository_no_description
                    )
            )

            Text(
                text = repository.language
                    ?: stringResource(
                        R.string.user_repository_no_language
                    ),
                style = MaterialTheme.typography.bodySmall
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    stringResource(
                        R.string.user_repository_stars,
                        repository.stars
                    )
                )

                Text(
                    stringResource(
                        R.string.user_repository_forks,
                        repository.forks
                    )
                )
            }

            // ADD OR REMOVE FAVORITE
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (isFavorite) {
                        favoritesRepository.removeFavorite(
                            repository.id
                        )
                    } else {
                        favoritesRepository.addFavorite(
                            repository
                        )
                    }

                    isFavorite = !isFavorite
                }
            ) {
                Text(
                    if (isFavorite) {
                        "♥ Remove from Favorites"
                    } else {
                        "♡ Add to Favorites"
                    }
                )
            }

            // OPEN GITHUB REPOSITORY
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(repository.htmlUrl)
                    )

                    try {
                        context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(
                            context,
                            R.string.user_no_app_available,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ) {
                Text(
                    stringResource(R.string.user_open_browser)
                )
            }

            // SHARE REPOSITORY
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val intent = Intent(
                        Intent.ACTION_SEND
                    ).apply {
                        type = "text/plain"
                        putExtra(
                            Intent.EXTRA_TEXT,
                            repository.htmlUrl
                        )
                    }

                    try {
                        context.startActivity(
                            Intent.createChooser(
                                intent,
                                shareTitle
                            )
                        )
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(
                            context,
                            R.string.user_no_app_available,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ) {
                Text(
                    stringResource(
                        R.string.user_repository_share
                    )
                )
            }
        }
    }
}
