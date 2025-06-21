package com.chesire.nekomp

import com.chesire.nekomp.library.datasource.mapping.MappingRepository

class Initializers(private val mappingRepository: MappingRepository) {

    suspend fun prepopulateDb() {
        // TODO: This can be removed when Room supports prepopulated in KMP
        mappingRepository.initialize()
    }
}
