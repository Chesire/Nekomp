package com.chesire.nekomp.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import co.touchlab.kermit.Logger
import com.chesire.nekomp.library.datasource.auth.AuthRepository
import com.chesire.nekomp.library.datasource.favorite.FavoriteRepository
import com.chesire.nekomp.library.datasource.stats.StatsRepository
import com.chesire.nekomp.library.datasource.user.UserRepository
import com.github.michaelbull.result.mapBoth
import com.github.michaelbull.result.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

private const val MAX_RETRIES = 3

class RefreshUserWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val favoriteRepository: FavoriteRepository,
    private val statsRepository: StatsRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Logger.d("RefreshUserWorker") { "Starting worker for refreshing user" }
        return if (authRepository.accessToken().isNullOrBlank()) {
            Logger.d("RefreshUserWorker") { "No user stored, exiting early" }
            Result.success()
        } else {
            withContext(Dispatchers.IO) {
                userRepository.retrieve()
                    .onSuccess {
                        awaitAll(
                            async { favoriteRepository.retrieveAnimeFavorites() },
                            async { favoriteRepository.retrieveMangaFavorites() },
                            async { favoriteRepository.retrieveCharacterFavorites() },
                            async { statsRepository.retrieveConsumedStats() }
                        )
                    }
                    .mapBoth(
                        success = {
                            Logger.d("RefreshUserWorker") { "Worker successful" }
                            Result.success()
                        },
                        failure = {
                            if (runAttemptCount < MAX_RETRIES) {
                                Logger.d("RefreshUserWorker") { "Worker failure, retrying" }
                                Result.retry()
                            } else {
                                Logger.d("RefreshUserWorker") { "Worker failure, not retrying" }
                                Result.failure()
                            }
                        }
                    )
            }
        }
    }
}
