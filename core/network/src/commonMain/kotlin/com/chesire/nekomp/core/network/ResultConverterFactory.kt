@file:Suppress("TooGenericExceptionCaught", "MagicNumber")

package com.chesire.nekomp.core.network

import co.touchlab.kermit.Logger
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.Converter
import de.jensklingenberg.ktorfit.converter.KtorfitResult
import de.jensklingenberg.ktorfit.converter.TypeData
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess

class ResultConverterFactory : Converter.Factory {

    override fun suspendResponseConverter(
        typeData: TypeData,
        ktorfit: Ktorfit
    ): Converter.SuspendResponseConverter<HttpResponse, *>? {
        return if (typeData.typeInfo.type == Result::class) {
            ResultConverter(typeData, ktorfit)
        } else {
            null
        }
    }

    internal class ResultConverter(
        private val typeData: TypeData,
        private val ktorfit: Ktorfit
    ) : Converter.SuspendResponseConverter<HttpResponse, Result<Any, NetworkError>> {

        override suspend fun convert(result: KtorfitResult): Result<Any, NetworkError> {
            return when (result) {
                is KtorfitResult.Success -> handleSuccess(result)
                is KtorfitResult.Failure -> handleFailure(result)
            }
        }

        @Suppress("TooGenericExceptionCaught")
        private suspend fun handleSuccess(success: KtorfitResult.Success): Result<Any, NetworkError> {
            val response = success.response
            return if (response.status.isSuccess()) {
                try {
                    val convertedBody = ktorfit
                        .nextSuspendResponseConverter(
                            null,
                            typeData.typeArgs.first()
                        )
                        ?.convert(success)
                        ?: response.body(typeData.typeArgs.first().typeInfo)
                    Ok(convertedBody)
                } catch (ex: Throwable) {
                    Logger.e("ResultConverterFactory", ex) {
                        "Issue when building result from success call"
                    }
                    Err(NetworkError.GenericError(ex))
                }
            } else {
                Err(NetworkError.ApiError(response.status.value, response.bodyAsText()))
            }
        }

        private fun handleFailure(failure: KtorfitResult.Failure): Result<Any, NetworkError> {
            Logger.e("ResultConverterFactory", failure.throwable) {
                "Got Ktorfit result failure"
            }
            return Err(NetworkError.GenericError(failure.throwable))
        }
    }
}
