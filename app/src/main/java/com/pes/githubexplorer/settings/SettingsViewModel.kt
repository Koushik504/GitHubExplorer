package com.pes.githubexplorer.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** State holder for the Settings screen. */
class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SettingsRepository(application)

    private val _darkThemeEnabled = MutableStateFlow(repository.isDarkThemeEnabled())
    val darkThemeEnabled: StateFlow<Boolean> = _darkThemeEnabled.asStateFlow()

    fun setDarkThemeEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setDarkThemeEnabled(enabled)
            _darkThemeEnabled.value = enabled
        }
    }
}
