package com.chesire.nekomp.feature.airing.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.chesire.nekomp.core.ext.airingAt
import com.chesire.nekomp.core.ext.toBestImage
import com.chesire.nekomp.core.ext.toChosenLanguage
import com.chesire.nekomp.core.preferences.ApplicationSettings
import com.chesire.nekomp.core.preferences.models.ImageQuality
import com.chesire.nekomp.core.preferences.models.TitleLanguage
import com.chesire.nekomp.library.datasource.airing.AiringAnime
import com.chesire.nekomp.library.datasource.airing.AiringRepository
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.isoDayNumber

class AiringViewModel(
    private val airingRepository: AiringRepository,
    private val applicationSettings: ApplicationSettings
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            airingRepository.currentAiring.collect { currentAiring ->
                Logger.d("AiringViewModel") { "Got new airing list - $currentAiring" }
                val imageQuality = applicationSettings.imageQuality.first()
                val titleLanguage = applicationSettings.titleLanguage.first()
                val airingItems = currentAiring.mapNotNull { item ->
                    val airingTime = item.airingTime?.airingAt() ?: return@mapNotNull null
                    item.toAiringItem(airingTime, imageQuality, titleLanguage)
                }
                // TODO: This will show series that are coming out in weeks in the future, we should fix that
                // So it will show all shows that it knows about on Mondays (even in 4 weeks time).
                _uiState.update { state ->
                    state.copy(
                        airingSeries = airingItems
                            .groupBy { it.airingTime.dayOfWeek }
                            .entries
                            .sortedBy { it.key.isoDayNumber }
                            .associate {
                                val values = it
                                    .value
                                    .sortedBy { it.airingTime.time }
                                    .toPersistentList()
                                it.key to values
                            }
                            .toPersistentMap()
                    )
                }
            }
        }
    }

    fun execute(action: ViewAction) {
        when (action) {
            ViewAction.ObservedViewEvent -> onObservedViewEvent()
        }
    }

    private fun onObservedViewEvent() {
        _uiState.update { state ->
            state.copy(viewEvent = null)
        }
    }

    private fun AiringAnime.toAiringItem(
        airingTime: LocalDateTime,
        imageQuality: ImageQuality,
        titleLanguage: TitleLanguage
    ): AiringItem {
        return AiringItem(
            title = titles.toChosenLanguage(titleLanguage),
            posterImage = posterImage.toBestImage(imageQuality),
            airingTime = airingTime
        )
    }
}
