package com.pes.githubexplorer.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pes.githubexplorer.GitHubRepository

/**
 * Favorites UI. The parent screen supplies the current list and actions.
 * This keeps the screen independent from the app's existing navigation.
 */
@Composable
fun FavoritesScreen(
    favorites: List<GitHubRepository>,
    onRemoveFavorite: (Long) -> Unit,
    onOpenRepository: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Favorite repositories", style = MaterialTheme.typography.headlineSmall)

        if (favorites.isEmpty()) {
            Text(
                text = "You haven't saved any repositories yet.",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Search for a GitHub user and save repositories to find them here.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Text("${favorites.size} saved", style = MaterialTheme.typography.bodySmall)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(favorites, key = { it.id }) { repository ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(repository.fullName, style = MaterialTheme.typography.titleMedium)
                            Text(
                                repository.description?.takeIf { it.isNotBlank() }
                                    ?: "No description provided",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                "Language: ${repository.language ?: "Not specified"}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text("★ ${repository.stars}    Forks: ${repository.forks}")
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(onClick = { onOpenRepository(repository.htmlUrl) }) {
                                    Text("Open")
                                }
                                OutlinedButton(onClick = { onRemoveFavorite(repository.id) }) {
                                    Text("Remove")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
