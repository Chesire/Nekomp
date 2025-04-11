@file:Suppress("TooGenericExceptionCaught", "MagicNumber")

package com.chesire.nekomp.core.network

import co.touchlab.kermit.Logger
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
        if (typeData.typeInfo.type == Result::class) {
            return object : Converter.SuspendResponseConverter<HttpResponse, Result<Any>> {
                override suspend fun convert(result: KtorfitResult): Result<Any> {
                    return when (result) {
                        is KtorfitResult.Success -> {
                            val response = result.response
                            return if (response.status.isSuccess()) {
                                try {
                                    val convertedBody = ktorfit
                                        .nextSuspendResponseConverter(
                                            null,
                                            typeData.typeArgs.first()
                                        )
                                        ?.convert(result)
                                        ?: response.body(typeData.typeArgs.first().typeInfo)
                                    Result.success(convertedBody)
                                } catch (ex: Throwable) {
                                    Logger.e("ResultConverterFactory", ex) {
                                        "Issue when building result from success call - $ex"
                                    }
                                    Result.failure(ex)
                                }
                            } else {
                                val body = response.bodyAsText()
                                Logger.w("ResultConverterFactory") {
                                    "Did not receive OK status, instead got ${response.status} with body of $body"
                                }
                                Result.failure(
                                    NetworkError.Api(
                                        response.status.value,
                                        body,
                                        response.status.description
                                    )
                                )
                            }
                        }

                        is KtorfitResult.Failure -> {
                            Logger.e("ResultConverterFactory", result.throwable) {
                                "Got Ktorfit result failure - ${result.throwable}"
                            }
                            Result.failure(NetworkError.Generic(result.throwable))
                        }
                    }
                }
            }
        }
        return null
    }
}
