package com.chesire.nekomp.library.datasource.mapping

import com.chesire.nekomp.library.datasource.mapping.local.MappingLocalDataSource

class MappingRepository(private val localDataSource: MappingLocalDataSource) {

    suspend fun initialize() {
        localDataSource.prepopulate()
    }
}
