package com.chesire.nekomp.core.network

import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.Converter
import de.jensklingenberg.ktorfit.converter.KtorfitResult
import de.jensklingenberg.ktorfit.converter.TypeData
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode

class EitherConverterFactory : Converter.Factory {

    override fun suspendResponseConverter(
        typeData: TypeData,
        ktorfit: Ktorfit
    ): Converter.SuspendResponseConverter<HttpResponse, *>? {
        if (typeData.typeInfo.type == Either::class) {
            return object : Converter.SuspendResponseConverter<HttpResponse, Any> {

                override suspend fun convert(result: KtorfitResult): Any {
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
                                    Either.success(convertedBody)
                                } catch (ex: Throwable) {
                                    Either.error(ex)
                                }

                                else -> Either.error(
                                    ApiError(
                                        response.status.value,
                                        response.status.description,
                                        response.bodyAsText()
                                    )
                                )
                            }
                        }

                        is KtorfitResult.Failure -> {
                            Either.error(result.throwable)
                        }
                    }
                }
            }
        }
        return null
    }
}
