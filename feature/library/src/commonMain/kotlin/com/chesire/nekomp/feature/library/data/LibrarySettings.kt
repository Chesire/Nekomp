package com.chesire.nekomp.feature.library.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.chesire.nekomp.core.model.EntryStatus
import com.chesire.nekomp.core.model.Type
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val VIEW_TYPE = "VIEW_TYPE"
private const val SORT_CHOICE = "SORT_CHOICE"
private const val TYPE_FILTER = "TYPE_FILTER"
private const val STATUS_FILTER = "STATUS_FILTER"

class LibrarySettings(private val preferences: DataStore<Preferences>) {

    val viewType: Flow<ViewType> = preferences.data.map {
        (it[stringPreferencesKey(VIEW_TYPE)] ?: ViewType.default.name).let {
            ViewType.fromString(it)
        }
    }

    val sortChoice: Flow<SortChoice> = preferences.data.map {
        (it[stringPreferencesKey(SORT_CHOICE)] ?: SortChoice.default.name).let {
            SortChoice.fromString(it)
        }
    }

    /**
     * Anime - true
     * Manga - true
     */
    private val defaultTypeFilter: ByteArray get() = byteArrayOf(1, 1)
    val typeFilter: Flow<Map<Type, Boolean>> = preferences.data.map {
        (it[byteArrayPreferencesKey(TYPE_FILTER)] ?: defaultTypeFilter).toFilterMap(Type.entries)
    }

    /**
     * Current - true
     * OnHold - false
     * Planned - false
     * Completed - false
     * Dropped - false
     */
    private val defaultStatusFilter: ByteArray get() = byteArrayOf(1, 0, 0, 0, 0)
    val statusFilter: Flow<Map<EntryStatus, Boolean>> = preferences.data.map {
        (it[byteArrayPreferencesKey(STATUS_FILTER)] ?: defaultStatusFilter)
            .toFilterMap(EntryStatus.entries)
    }

    suspend fun updateViewType(newViewType: ViewType) {
        preferences.edit {
            it[stringPreferencesKey(VIEW_TYPE)] = newViewType.name
        }
    }

    suspend fun updateSortChoice(newSortChoice: SortChoice) {
        preferences.edit {
            it[stringPreferencesKey(SORT_CHOICE)] = newSortChoice.name
        }
    }

    suspend fun updateTypeFilter(newTypeFilters: Map<Type, Boolean>) {
        preferences.edit {
            it[byteArrayPreferencesKey(TYPE_FILTER)] = newTypeFilters.toBytes()
        }
    }

    suspend fun updateStatusFilter(newStatusFilters: Map<EntryStatus, Boolean>) {
        preferences.edit {
            it[byteArrayPreferencesKey(STATUS_FILTER)] = newStatusFilters.toBytes()
        }
    }

    private fun <T : Enum<T>> ByteArray.toFilterMap(entries: List<T>): Map<T, Boolean> {
        return mapIndexed { index, byte ->
            entries[index] to (byte != 0.toByte())
        }.toMap()
    }

    private fun <T : Enum<T>> Map<T, Boolean>.toBytes(): ByteArray {
        return entries
            .map { (_, value) ->
                (if (value) 1 else 0).toByte()
            }
            .toByteArray()
    }
}
