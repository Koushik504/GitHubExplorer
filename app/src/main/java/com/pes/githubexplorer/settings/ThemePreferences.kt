package com.pes.githubexplorer.settings

import android.content.Context

/**
 * Small helper for reading/writing the selected theme.
 * The app must pass the returned value to GitHubExplorerTheme(darkTheme = ...).
 */
object ThemePreferences {
    private const val PREFERENCES_NAME = "github_explorer_settings"
    private const val KEY_DARK_THEME = "dark_theme_enabled"

    fun isDarkThemeEnabled(context: Context): Boolean =
        context.applicationContext
            .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_DARK_THEME, false)

    fun setDarkThemeEnabled(context: Context, enabled: Boolean) {
        context.applicationContext
            .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_DARK_THEME, enabled)
            .apply()
    }
}
