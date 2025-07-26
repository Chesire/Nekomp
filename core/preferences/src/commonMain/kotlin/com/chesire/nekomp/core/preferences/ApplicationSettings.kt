package com.chesire.nekomp.core.preferences

import com.chesire.nekomp.core.preferences.models.ImageQuality
import com.chesire.nekomp.core.preferences.models.Theme
import com.chesire.nekomp.core.preferences.models.TitleLanguage
import kotlinx.coroutines.flow.Flow

interface ApplicationSettings {

    val theme: Flow<Theme>
    val imageQuality: Flow<ImageQuality>
    val titleLanguage: Flow<TitleLanguage>
    val rateOnFinish: Flow<Boolean>

    suspend fun updateTheme(newTheme: Theme)
    suspend fun updateImageQuality(newImageQuality: ImageQuality)
    suspend fun updateTitleLanguage(newTitleLanguage: TitleLanguage)
    suspend fun updateRateOnFinish(newValue: Boolean)
}
