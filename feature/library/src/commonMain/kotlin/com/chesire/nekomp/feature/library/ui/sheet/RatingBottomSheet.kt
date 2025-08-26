@file:OptIn(ExperimentalMaterial3Api::class)

package com.chesire.nekomp.feature.library.ui.sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.chesire.nekomp.core.resources.NekoRes
import com.chesire.nekomp.feature.library.ui.LibraryBottomSheet
import nekomp.core.resources.generated.resources.library_detail_rating_sheet_status_current_rating
import nekomp.core.resources.generated.resources.library_detail_rating_sheet_status_no_rating
import nekomp.core.resources.generated.resources.library_detail_rating_sheet_title
import nekomp.core.resources.generated.resources.library_detail_sheet_cancel_cta
import nekomp.core.resources.generated.resources.library_detail_sheet_update_cta
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun RatingBottomSheet(
    sheetState: SheetState,
    currentRating: Int,
    seriesTitle: String,
    state: LibraryBottomSheet.BottomSheetState,
    execute: (Int?) -> Unit
) {
    var dirtyRating by remember { mutableStateOf(currentRating) }
    val ratingText = if (currentRating > 0) {
        stringResource(
            NekoRes.string.library_detail_rating_sheet_status_current_rating,
            currentRating / 2f
        )
    } else {
        stringResource(NekoRes.string.library_detail_rating_sheet_status_no_rating)
    }

    ModalBottomSheet(
        onDismissRequest = { execute(null) },
        modifier = Modifier,
        sheetState = sheetState,
        content = {
            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(NekoRes.string.library_detail_rating_sheet_title),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = seriesTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = ratingText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // CONTENT

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { execute(null) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(stringResource(NekoRes.string.library_detail_sheet_cancel_cta))
                    }

                    Button(
                        onClick = { execute(dirtyRating) },
                        enabled = state !is LibraryBottomSheet.BottomSheetState.Updating &&
                            currentRating != dirtyRating,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(stringResource(NekoRes.string.library_detail_sheet_update_cta))
                    }
                }
            }
        }
    )
}
