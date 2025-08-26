@file:OptIn(ExperimentalMaterial3Api::class)

package com.chesire.nekomp.feature.library.ui.sheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import com.chesire.nekomp.feature.library.ui.LibraryBottomSheet

@Composable
internal fun RatingBottomSheet(
    sheetState: SheetState,
    currentRating: Int,
    seriesTitle: String,
    state: LibraryBottomSheet.BottomSheetState,
    execute: (Int?) -> Unit
) {

}
