package com.chesire.nekomp.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AuthEventHandler {

    private val _userLoggedOutEvent = MutableSharedFlow<Unit>()

    val userLoggedOutEvent: SharedFlow<Unit> = _userLoggedOutEvent.asSharedFlow()

    suspend fun emitUserLoggedOut() {
        _userLoggedOutEvent.emit(Unit)
    }
}
