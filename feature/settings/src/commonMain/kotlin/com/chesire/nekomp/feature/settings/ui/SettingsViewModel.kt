package com.chesire.nekomp.feature.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekomp.core.coroutines.combine
import com.chesire.nekomp.core.preferences.ApplicationSettings
import com.chesire.nekomp.core.preferences.ImageQuality
import com.chesire.nekomp.core.preferences.Theme
import com.chesire.nekomp.core.preferences.TitleLanguage
import com.chesire.nekomp.feature.settings.core.LogoutExecutor
import com.chesire.nekomp.feature.settings.data.ApplicationVersionInfo
import com.chesire.nekomp.feature.settings.ui.SettingsBottomSheet.ImageQualityBottomSheet
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val applicationSettings: ApplicationSettings,
    private val applicationVersionInfo: ApplicationVersionInfo,
    private val logout: LogoutExecutor
) : ViewModel() {

    private val _appVersion =
        "${applicationVersionInfo.versionName} (${applicationVersionInfo.versionCode})"
    private val _bottomSheet = MutableStateFlow<SettingsBottomSheet?>(null)
    private val _viewEvent = MutableStateFlow<ViewEvent?>(null)

    val uiState = combine(
        applicationSettings.theme,
        applicationSettings.titleLanguage,
        applicationSettings.imageQuality,
        applicationSettings.rateOnFinish,
        _viewEvent,
        _bottomSheet
    ) { theme, titleLanguage, imageQuality, rateOnFinish, viewEvent, bottomSheet ->
        UIState(
            currentTheme = theme.name,
            titleLanguage = titleLanguage.name,
            imageQuality = imageQuality.name,
            rateChecked = rateOnFinish,
            version = _appVersion,
            viewEvent = viewEvent,
            bottomSheet = bottomSheet
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UIState()
    )

    fun execute(action: ViewAction) {
        when (action) {
            ViewAction.ThemeClick -> onThemeClick()
            is ViewAction.ThemeChosen -> onThemeChosen(action.theme)

            ViewAction.TitleLanguageClick -> onTitleLanguageClick()
            is ViewAction.TitleLanguageChosen -> onTitleLanguageChosen(action.titleLanguage)

            ViewAction.ImageQualityClick -> onImageQualityClick()
            is ViewAction.ImageQualityChosen -> onImageQualityChosen(action.imageQuality)

            ViewAction.RateChanged -> onRateChanged()

            ViewAction.LogoutClick -> onLogoutClick()
            ViewAction.ObservedViewEvent -> onObservedViewEvent()
        }
    }

    private fun onThemeClick() = viewModelScope.launch {
        _bottomSheet.update {
            SettingsBottomSheet.ThemeBottomSheet(
                themes = Theme.entries.toPersistentList(),
                selectedTheme = applicationSettings.theme.first()
            )
        }
    }

    private fun onThemeChosen(newTheme: Theme?) = viewModelScope.launch {
        if (newTheme != null) {
            applicationSettings.updateTheme(newTheme)
        }

        _bottomSheet.update { null }
    }

    private fun onTitleLanguageClick() = viewModelScope.launch {
        _bottomSheet.update {
            SettingsBottomSheet.TitleLanguageBottomSheet(
                languages = TitleLanguage.entries.toPersistentList(),
                selectedLanguage = applicationSettings.titleLanguage.first()
            )
        }
    }

    private fun onTitleLanguageChosen(newTitleLanguage: TitleLanguage?) = viewModelScope.launch {
        if (newTitleLanguage != null) {
            applicationSettings.updateTitleLanguage(newTitleLanguage)
        }

        _bottomSheet.update { null }
    }

    private fun onImageQualityClick() = viewModelScope.launch {
        _bottomSheet.update {
            ImageQualityBottomSheet(
                qualities = ImageQuality.entries.toPersistentList(),
                selectedQuality = applicationSettings.imageQuality.first()
            )
        }
    }

    private fun onImageQualityChosen(newImageQuality: ImageQuality?) = viewModelScope.launch {
        if (newImageQuality != null) {
            applicationSettings.updateImageQuality(newImageQuality)
        }

        _bottomSheet.update { null }
    }

    private fun onRateChanged() = viewModelScope.launch {
        applicationSettings.updateRateOnFinish(!uiState.value.rateChecked)
    }

    private fun onLogoutClick() = viewModelScope.launch {
        logout.execute()
        _viewEvent.update { ViewEvent.LoggedOut }
    }

    private fun onObservedViewEvent() = _viewEvent.update { null }
}
