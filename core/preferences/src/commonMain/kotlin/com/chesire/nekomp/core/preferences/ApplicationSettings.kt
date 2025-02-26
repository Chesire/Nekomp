package com.chesire.nekomp.core.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val THEME = "THEME_KEY"
private const val IMAGE_QUALITY = "IMAGE_QUALITY_KEY"
private const val TITLE_LANGUAGE = "TITLE_LANGUAGE_KEY"
private const val RATE_ON_FINISH = "RATE_ON_FINISH_KEY"

class ApplicationSettings(private val preferences: DataStore<Preferences>) {

    val theme: Flow<Theme> = preferences.data.map {
        (it[stringPreferencesKey(THEME)] ?: Theme.default.name).let {
            Theme.fromString(it)
        }
    }

    val imageQuality: Flow<ImageQuality> = preferences.data.map {
        (it[stringPreferencesKey(IMAGE_QUALITY)] ?: ImageQuality.default.name).let {
            ImageQuality.fromString(it)
        }
    }

    val titleLanguage: Flow<TitleLanguage> = preferences.data.map {
        (it[stringPreferencesKey(TITLE_LANGUAGE)] ?: TitleLanguage.default.name).let {
            TitleLanguage.fromString(it)
        }
    }

    val rateOnFinish: Flow<Boolean> = preferences.data.map {
        it[booleanPreferencesKey(RATE_ON_FINISH)] == true
    }

    suspend fun updateTheme(newTheme: Theme) {
        preferences.edit {
            it[stringPreferencesKey(THEME)] = newTheme.name
        }
    }

    suspend fun updateImageQuality(newImageQuality: ImageQuality) {
        preferences.edit {
            it[stringPreferencesKey(IMAGE_QUALITY)] = newImageQuality.name
        }
    }

    suspend fun updateTitleLanguage(newTitleLanguage: TitleLanguage) {
        preferences.edit {
            it[stringPreferencesKey(TITLE_LANGUAGE)] = newTitleLanguage.name
        }
    }

    suspend fun updateRateOnFinish(newValue: Boolean) {
        preferences.edit {
            it[booleanPreferencesKey(RATE_ON_FINISH)] = newValue
        }
    }
}

enum class Theme {
    System,
    Light,
    Dark;

    companion object {

        internal val default: Theme = System

        fun fromString(input: String): Theme {
            return Theme.entries.find { it.name.lowercase() == input.lowercase() } ?: default
        }
    }
}

enum class ImageQuality {
    Lowest,
    Low,
    Medium,
    High,
    Highest;

    companion object {

        internal val default: ImageQuality = Medium

        fun fromString(input: String): ImageQuality {
            return ImageQuality.entries.find { it.name.lowercase() == input.lowercase() } ?: default
        }
    }
}

enum class TitleLanguage {
    Canonical,
    English,
    Romaji,
    CJK;

    companion object {

        internal val default: TitleLanguage = Canonical

        fun fromString(input: String): TitleLanguage {
            return TitleLanguage
                .entries
                .find { it.name.lowercase() == input.lowercase() }
                ?: default
        }
    }
}
