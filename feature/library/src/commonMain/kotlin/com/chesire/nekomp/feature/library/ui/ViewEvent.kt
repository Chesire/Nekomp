package com.chesire.nekomp.feature.library.ui

sealed interface ViewEvent {
    data class SeriesUpdated(val message: String) : ViewEvent
    data class SeriesUpdateFailed(val message: String) : ViewEvent
}
