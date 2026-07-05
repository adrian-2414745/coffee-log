package io.github.adrian2414745.coffeelog.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

/**
 * Dark-theme preference in Preferences DataStore.
 * `null` = not yet set → follow system dark mode; `true`/`false` = explicit user choice.
 * Deliberately excluded from JSON export/import (device-local).
 */
class ThemeRepository(private val context: Context) {
    private val key = booleanPreferencesKey("dark_theme")

    val darkTheme: Flow<Boolean?> = context.dataStore.data.map { it[key] }

    suspend fun setDarkTheme(enabled: Boolean) {
        context.dataStore.edit { it[key] = enabled }
    }
}
