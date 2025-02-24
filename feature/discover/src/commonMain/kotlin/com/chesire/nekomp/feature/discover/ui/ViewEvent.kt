package com.chesire.nekomp.feature.discover.ui

sealed interface ViewEvent {
    data class ShowFailure(val errorString: String) : ViewEvent
}
