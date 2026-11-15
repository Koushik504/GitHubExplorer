
package com.pes.githubexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pes.githubexplorer.favorites.FavoritesScreen
import com.pes.githubexplorer.favorites.FavoritesViewModel
import com.pes.githubexplorer.settings.SettingsScreen
import com.pes.githubexplorer.settings.SettingsViewModel
import com.pes.githubexplorer.ui.theme.GitHubExplorerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            val favoritesViewModel: FavoritesViewModel = viewModel()
            val settingsViewModel: SettingsViewModel = viewModel()

            val favorites by favoritesViewModel.favorites.collectAsState()
            val darkThemeEnabled by settingsViewModel.darkThemeEnabled.collectAsState()

            var selectedUsername by rememberSaveable {
                mutableStateOf<String?>(null)
            }

            var selectedTab by rememberSaveable {
                mutableStateOf("Search")
            }

            val uriHandler = LocalUriHandler.current

            GitHubExplorerTheme(
                darkTheme = darkThemeEnabled
            ) {

                LaunchedEffect(selectedTab) {
                    if (selectedTab == "Favorites") {
                        favoritesViewModel.refresh()
                    }
                }

                BackHandler(
                    enabled = selectedUsername != null ||
                            selectedTab != "Search"
                ) {
                    if (selectedUsername != null) {
                        selectedUsername = null
                    } else {
                        selectedTab = "Search"
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),

                    bottomBar = {
                        if (selectedUsername == null) {
                            NavigationBar {

                                NavigationBarItem(
                                    selected = selectedTab == "Search",
                                    onClick = {
                                        selectedTab = "Search"
                                    },
                                    icon = { Text("⌕") },
                                    label = { Text("Search") }
                                )

                                NavigationBarItem(
                                    selected = selectedTab == "Favorites",
                                    onClick = {
                                        selectedTab = "Favorites"
                                    },
                                    icon = { Text("♥") },
                                    label = { Text("Favorites") }
                                )

                                NavigationBarItem(
                                    selected = selectedTab == "Settings",
                                    onClick = {
                                        selectedTab = "Settings"
                                    },
                                    icon = { Text("⚙") },
                                    label = { Text("Settings") }
                                )
                            }
                        }
                    }
                ) { innerPadding ->

                    val contentModifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)

                    if (selectedUsername != null) {

                        UserProfileScreen(
                            username = selectedUsername!!,
                            modifier = contentModifier,
                            onBack = {
                                selectedUsername = null
                            }
                        )

                    } else {

                        when (selectedTab) {

                            "Search" -> {
                                UserSearchScreen(
                                    modifier = contentModifier,
                                    onUserClick = { selectedUser ->
                                        selectedUsername = selectedUser
                                    }
                                )
                            }

                            "Favorites" -> {
                                FavoritesScreen(
                                    favorites = favorites,
                                    onRemoveFavorite = { repositoryId ->
                                        favoritesViewModel.removeFavorite(
                                            repositoryId
                                        )
                                    },
                                    onOpenRepository = { url ->
                                        uriHandler.openUri(url)
                                    },
                                    modifier = contentModifier
                                )
                            }

                            "Settings" -> {
                                SettingsScreen(
                                    darkThemeEnabled = darkThemeEnabled,
                                    onDarkThemeChanged = { enabled ->
                                        settingsViewModel.setDarkThemeEnabled(
                                            enabled
                                        )
                                    },
                                    onClearFavorites = {
                                        favoritesViewModel.clearFavorites()
                                    },
                                    modifier = contentModifier
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
