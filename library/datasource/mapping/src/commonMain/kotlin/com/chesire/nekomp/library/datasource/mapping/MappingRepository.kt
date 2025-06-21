package com.chesire.nekomp.library.datasource.mapping

import com.chesire.nekomp.library.datasource.mapping.local.MappingLocalDataSource
import com.github.michaelbull.result.Result

class MappingRepository(private val localDataSource: MappingLocalDataSource) {

    suspend fun initialize() {
        localDataSource.prepopulate()
    }

    suspend fun updateMappings(): Result<Unit, Unit> {
        
    }
}
