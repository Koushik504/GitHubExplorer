package com.pes.githubexplorer.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Settings UI. Theme state is supplied by the parent and persisted by SettingsViewModel.
 * onClearFavorites should call FavoritesViewModel.clearFavorites().
 */
@Composable
fun SettingsScreen(
    darkThemeEnabled: Boolean,
    onDarkThemeChanged: (Boolean) -> Unit,
    onClearFavorites: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showClearConfirmation by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineSmall)

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Dark mode", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Use a dark appearance for GitHub Explorer.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(checked = darkThemeEnabled, onCheckedChange = onDarkThemeChanged)
        }

        HorizontalDivider()

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Favorites", style = MaterialTheme.typography.titleMedium)
            Text(
                "Remove all repositories saved in your Favorites list.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            OutlinedButton(onClick = { showClearConfirmation = true }) {
                Text("Clear all favorites")
            }
        }

        HorizontalDivider()

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("About", style = MaterialTheme.typography.titleMedium)
            Text("GitHub Repository Explorer", style = MaterialTheme.typography.bodyMedium)
            Text("Version 1.0", style = MaterialTheme.typography.bodySmall)
        }
    }

    if (showClearConfirmation) {
        AlertDialog(
            onDismissRequest = { showClearConfirmation = false },
            title = { Text("Clear all favorites?") },
            text = { Text("This will remove every saved repository. This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    onClearFavorites()
                    showClearConfirmation = false
                }) { Text("Clear") }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
