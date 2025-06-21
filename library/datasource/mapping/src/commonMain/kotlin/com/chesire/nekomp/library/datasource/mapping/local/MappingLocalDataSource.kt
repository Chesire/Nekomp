package com.chesire.nekomp.library.datasource.mapping.local

import co.touchlab.kermit.Logger
import com.chesire.nekomp.core.database.dao.MappingDao
import com.chesire.nekomp.core.database.entity.MappingEntity
import com.chesire.nekomp.core.resources.NekoRes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class MappingLocalDataSource(private val mappingDao: MappingDao) {

    // TODO: This can be removed when Room supports prepopulated in KMP
    suspend fun prepopulate() {
        Logger.d("MappingLocalDataSource") { "Prepopulating the MapperDao" }
        if (mappingDao.exists()) {
            Logger.d("MappingLocalDataSource") { "Skipping prepopulating as already exists" }
        } else {
            val json = Json { explicitNulls = false }
            val value = NekoRes
                .readBytes("files/anime-list-mini.json")
                .decodeToString()
            val items = json
                .decodeFromString<List<Mapping>>(value)
                .map {
                    MappingEntity(malId = it.malId, kitsuId = it.kitsuId, aniListId = it.aniListId)
                }
            mappingDao.upsert(items)
            Logger.d("MappingLocalDataSource") { "MapperDao now populated" }
        }
    }

    @Serializable
    data class Mapping(
        @SerialName("mal_id")
        val malId: Int?,
        @SerialName("kitsu_id")
        val kitsuId: Int?,
        @SerialName("anilist_id")
        val aniListId: Int?
    )
}
