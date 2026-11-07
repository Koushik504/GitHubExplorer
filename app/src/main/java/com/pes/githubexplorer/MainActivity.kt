package com.pes.githubexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.pes.githubexplorer.ui.theme.GitHubExplorerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GitHubExplorerTheme {
                var selectedUsername by rememberSaveable {
                    mutableStateOf<String?>(null)
                }

                BackHandler(enabled = selectedUsername != null) {
                    selectedUsername = null
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    val username = selectedUsername

                    if (username == null) {
                        UserSearchScreen(
                            modifier = Modifier.padding(innerPadding),
                            onUserClick = { selectedUser ->
                                selectedUsername = selectedUser
                            }
                        )
                    } else {
                        UserProfileScreen(
                            username = username,
                            modifier = Modifier.padding(innerPadding),
                            onBack = {
                                selectedUsername = null
                            }
                        )
                    }
                }
            }
        }
    }
}