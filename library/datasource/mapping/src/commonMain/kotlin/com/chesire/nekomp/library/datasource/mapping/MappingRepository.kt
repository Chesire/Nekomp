package com.chesire.nekomp.library.datasource.mapping

import com.chesire.nekomp.library.datasource.mapping.local.MappingLocalDataSource
import com.chesire.nekomp.library.datasource.mapping.remote.MappingRemoteDataSource
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import com.github.michaelbull.result.onSuccess

class MappingRepository(
    private val localDataSource: MappingLocalDataSource,
    private val remoteDataSource: MappingRemoteDataSource
) {

    suspend fun initialize() {
        localDataSource.prepopulate()
    }

    suspend fun updateMappings(): Result<Unit, Unit> {
        return remoteDataSource.requestNewMappings()
            .onSuccess {
                localDataSource.updateMappings(it)
            }
            .map { Unit }
    }
}
