package com.chesire.nekomp.library.datasource.mapping.remote

import co.touchlab.kermit.Logger
import com.chesire.nekomp.library.datasource.mapping.dto.MappingDto
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.readRawBytes
import kotlinx.serialization.json.Json

private const val MAPPING_URL =
    "https://raw.githubusercontent.com/Chesire/Nekomp/refs/heads/master/core/resources/src/commonMain/composeResources/files/anime-list-mini.json"

class MappingRemoteDataSource(
    private val httpClient: HttpClient,
    private val json: Json
) {

    suspend fun requestNewMappings(): Result<List<MappingDto>, Unit> {
        return try {
            val stringResult = httpClient
                .get(MAPPING_URL)
                .readRawBytes()
                .decodeToString()
            val mappings = json.decodeFromString<List<MappingDto>>(stringResult)
            if (mappings.isEmpty()) {
                Err(Unit)
            } else {
                Ok(mappings)
            }
        } catch (ex: Exception) {
            Logger.e("MappingRemoteDataSource", ex) { "Failed to pull down and parse the mappings" }
            Err(Unit)
        }
    }
}
