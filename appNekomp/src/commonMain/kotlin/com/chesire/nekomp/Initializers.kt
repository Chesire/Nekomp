package com.chesire.nekomp

import com.chesire.nekomp.library.datasource.mapping.MappingRepository

class Initializers(private val mappingRepository: MappingRepository) {

    // TODO: This can be removed when Room supports prepopulated in KMP
    suspend fun prepopulateDb() {
        mappingRepository.initialize()
    }
}
