package com.chesire.nekomp.api

import com.chesire.nekomp.model.LoginRequestDto
import com.chesire.nekomp.model.LoginResponseDto
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.Converter
import de.jensklingenberg.ktorfit.converter.KtorfitResult
import de.jensklingenberg.ktorfit.converter.TypeData
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode

interface AuthApi {

    @Headers(
        //"Accept: application/vnd.api+json",
        "Content-Type: application/json"
    )
    @POST("api/oauth/token")
    suspend fun login(@Body body: LoginRequestDto): Either<LoginResponseDto>
}

sealed class Either<T> {
    data class Success<T>(val data: T) : Either<T>()
    class Error(val ex: Throwable) : Either<Nothing>()

    companion object {
        fun <T> success(data: T) = Success(data)
        fun error(ex: Throwable) = Error(ex)
    }
}

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

data class ApiError(
    val code: Int,
    val reason: String = "",
    val body: String
) : Throwable()
