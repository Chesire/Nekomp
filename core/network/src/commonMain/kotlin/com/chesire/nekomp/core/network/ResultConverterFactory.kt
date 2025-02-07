@file:Suppress("TooGenericExceptionCaught")

package com.chesire.nekomp.core.network

import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.Converter
import de.jensklingenberg.ktorfit.converter.KtorfitResult
import de.jensklingenberg.ktorfit.converter.TypeData
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode

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
                            return when (response.status) {
                                HttpStatusCode.OK -> try {
                                    val convertedBody = ktorfit
                                        .nextSuspendResponseConverter(
                                            null,
                                            typeData.typeArgs.first()
                                        )
                                        ?.convert(result)
                                        ?: response.body(typeData.typeArgs.first().typeInfo)
                                    Result.success(convertedBody)
                                } catch (ex: Throwable) {
                                    Result.failure(ex)
                                }

                                else -> Result.failure(
                                    ApiError(
                                        response.status.value,
                                        response.bodyAsText(),
                                        response.status.description
                                    )
                                )
                            }
                        }

                        is KtorfitResult.Failure -> Result.failure(result.throwable)
                    }
                }
            }
        }
        return null
    }
}
