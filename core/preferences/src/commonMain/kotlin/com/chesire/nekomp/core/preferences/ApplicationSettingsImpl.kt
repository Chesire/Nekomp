package com.chesire.nekomp.core.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.chesire.nekomp.core.preferences.models.ImageQuality
import com.chesire.nekomp.core.preferences.models.Theme
import com.chesire.nekomp.core.preferences.models.TitleLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val THEME = "THEME_KEY"
private const val IMAGE_QUALITY = "IMAGE_QUALITY_KEY"
private const val TITLE_LANGUAGE = "TITLE_LANGUAGE_KEY"
private const val RATE_ON_FINISH = "RATE_ON_FINISH_KEY"

class ApplicationSettingsImpl(private val preferences: DataStore<Preferences>) :
    ApplicationSettings {

    override val theme: Flow<Theme> = preferences.data.map {
        (it[stringPreferencesKey(THEME)] ?: Theme.default.name).let {
            Theme.fromString(it)
        }
    }

    override val imageQuality: Flow<ImageQuality> = preferences.data.map {
        (it[stringPreferencesKey(IMAGE_QUALITY)] ?: ImageQuality.default.name).let {
            ImageQuality.fromString(it)
        }
    }

    override val titleLanguage: Flow<TitleLanguage> = preferences.data.map {
        (it[stringPreferencesKey(TITLE_LANGUAGE)] ?: TitleLanguage.default.name).let {
            TitleLanguage.fromString(it)
        }
    }

    override val rateOnFinish: Flow<Boolean> = preferences.data.map {
        it[booleanPreferencesKey(RATE_ON_FINISH)] == true
    }

    override suspend fun updateTheme(newTheme: Theme) {
        preferences.edit {
            it[stringPreferencesKey(THEME)] = newTheme.name
        }
    }

    override suspend fun updateImageQuality(newImageQuality: ImageQuality) {
        preferences.edit {
            it[stringPreferencesKey(IMAGE_QUALITY)] = newImageQuality.name
        }
    }

    override suspend fun updateTitleLanguage(newTitleLanguage: TitleLanguage) {
        preferences.edit {
            it[stringPreferencesKey(TITLE_LANGUAGE)] = newTitleLanguage.name
        }
    }

    override suspend fun updateRateOnFinish(newValue: Boolean) {
        preferences.edit {
            it[booleanPreferencesKey(RATE_ON_FINISH)] = newValue
        }
    }
}
