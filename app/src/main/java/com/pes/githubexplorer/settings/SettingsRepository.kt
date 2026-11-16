package com.pes.githubexplorer.settings

import android.content.Context

/** Stores app preferences using Android SharedPreferences. */
class SettingsRepository(context: Context) {
    private val preferences = context.applicationContext
        .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun isDarkThemeEnabled(): Boolean =
        preferences.getBoolean(KEY_DARK_THEME, false)

    fun setDarkThemeEnabled(enabled: Boolean) {
        preferences.edit().putBoolean(KEY_DARK_THEME, enabled).apply()
    }

    private companion object {
        const val PREFERENCES_NAME = "github_explorer_settings"
        const val KEY_DARK_THEME = "dark_theme_enabled"
    }
}
