package com.chesire.nekomp.feature.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekomp.core.preferences.ApplicationSettings
import com.chesire.nekomp.core.preferences.ImageQuality
import com.chesire.nekomp.core.preferences.Theme
import com.chesire.nekomp.core.preferences.TitleLanguage
import com.chesire.nekomp.feature.settings.core.LogoutExecutor
import com.chesire.nekomp.feature.settings.ui.SettingsBottomSheet.ImageQualityBottomSheet
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val applicationSettings: ApplicationSettings,
    private val logout: LogoutExecutor
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            applicationSettings.theme.collect { theme ->
                _uiState.update { state ->
                    state.copy(currentTheme = theme.name)
                }
            }
        }
        viewModelScope.launch {
            applicationSettings.imageQuality.collect { imageQuality ->
                _uiState.update { state ->
                    state.copy(imageQuality = imageQuality.name)
                }
            }
        }
        viewModelScope.launch {
            applicationSettings.titleLanguage.collect { titleLanguage ->
                _uiState.update { state ->
                    state.copy(titleLanguage = titleLanguage.name)
                }
            }
        }
        viewModelScope.launch {
            applicationSettings.rateOnFinish.collect { rateOnFinish ->
                _uiState.update { state ->
                    state.copy(rateChecked = rateOnFinish)
                }
            }
        }
    }

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
        _uiState.update { state ->
            state.copy(
                bottomSheet = SettingsBottomSheet.ThemeBottomSheet(
                    themes = Theme.entries.toPersistentList(),
                    selectedTheme = applicationSettings.theme.first()
                )
            )
        }
    }

    private fun onThemeChosen(newTheme: Theme?) = viewModelScope.launch {
        if (newTheme != null) {
            applicationSettings.updateTheme(newTheme)
        }

        _uiState.update { state ->
            state.copy(bottomSheet = null)
        }
    }

    private fun onTitleLanguageClick() = viewModelScope.launch {
        _uiState.update { state ->
            state.copy(
                bottomSheet = SettingsBottomSheet.TitleLanguageBottomSheet(
                    languages = TitleLanguage.entries.toPersistentList(),
                    selectedLanguage = applicationSettings.titleLanguage.first()
                )
            )
        }
    }

    private fun onTitleLanguageChosen(newTitleLanguage: TitleLanguage?) = viewModelScope.launch {
        if (newTitleLanguage != null) {
            applicationSettings.updateTitleLanguage(newTitleLanguage)
        }

        _uiState.update { state ->
            state.copy(bottomSheet = null)
        }
    }

    private fun onImageQualityClick() = viewModelScope.launch {
        _uiState.update { state ->
            state.copy(
                bottomSheet = ImageQualityBottomSheet(
                    qualities = ImageQuality.entries.toPersistentList(),
                    selectedQuality = applicationSettings.imageQuality.first()
                )
            )
        }
    }

    private fun onImageQualityChosen(newImageQuality: ImageQuality?) = viewModelScope.launch {
        if (newImageQuality != null) {
            applicationSettings.updateImageQuality(newImageQuality)
        }

        _uiState.update { state ->
            state.copy(bottomSheet = null)
        }
    }

    private fun onRateChanged() = viewModelScope.launch {
        applicationSettings.updateRateOnFinish(!_uiState.value.rateChecked)
    }

    private fun onLogoutClick() = viewModelScope.launch {
        logout.execute()
        _uiState.update { state ->
            state.copy(viewEvent = ViewEvent.LoggedOut)
        }
    }

    private fun onObservedViewEvent() {
        _uiState.update {
            it.copy(viewEvent = null)
        }
    }
}
