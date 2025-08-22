@file:OptIn(ExperimentalMaterial3Api::class)

package com.chesire.nekomp.feature.library.ui.sheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import com.chesire.nekomp.core.model.EntryStatus
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.feature.library.ui.LibraryBottomSheet
import kotlinx.collections.immutable.PersistentSet

@Composable
internal fun StatusBottomSheet(
    sheetState: SheetState,
    currentStatus: EntryStatus,
    allStatus: PersistentSet<EntryStatus>,
    seriesTitle: String,
    seriesType: Type,
    state: LibraryBottomSheet.BottomSheetState,
    execute: (EntryStatus?) -> Unit
) {

}
