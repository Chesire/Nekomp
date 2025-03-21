package com.chesire.nekomp

import co.touchlab.kermit.Logger
import com.chesire.nekomp.core.database.dao.MappingDao
import com.chesire.nekomp.core.database.entity.MappingEntity
import com.chesire.nekomp.core.resources.NekoRes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class Initializers(private val mappingDao: MappingDao) {

    // TODO: This can be removed when Room supports prepopulated in KMP
    suspend fun prepopulateDb() {
        Logger.d("Initializers") { "Prepopulating the MapperDao" }
        if (mappingDao.exists()) {
            Logger.d("Initializers") { "Skipping prepopulating as already exists" }
        } else {
            val json = Json { explicitNulls = false }
            val value = NekoRes
                .readBytes("files/anime-list-mini.json")
                .decodeToString()
            val items = json
                .decodeFromString<List<Mapping>>(value)
                .map {
                    MappingEntity(malId = it.malId, kitsuId = it.kitsuId)
                }
            mappingDao.upsert(items)
            Logger.d("Initializers") { "MapperDao now populated" }
        }
    }

    @Serializable
    data class Mapping(
        @SerialName("mal_id")
        val malId: Int?,
        @SerialName("kitsu_id")
        val kitsuId: Int?
    )
}
