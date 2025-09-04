@file:OptIn(ExperimentalMaterial3Api::class)

package com.chesire.nekomp.feature.library.ui.sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.chesire.nekomp.core.resources.NekoRes
import com.chesire.nekomp.feature.library.ui.LibraryBottomSheet
import kotlin.math.roundToInt
import nekomp.core.resources.generated.resources.library_detail_rating_sheet_status_0
import nekomp.core.resources.generated.resources.library_detail_rating_sheet_status_10
import nekomp.core.resources.generated.resources.library_detail_rating_sheet_status_current_rating
import nekomp.core.resources.generated.resources.library_detail_rating_sheet_status_lt_10
import nekomp.core.resources.generated.resources.library_detail_rating_sheet_status_lt_3
import nekomp.core.resources.generated.resources.library_detail_rating_sheet_status_lt_5
import nekomp.core.resources.generated.resources.library_detail_rating_sheet_status_lt_6
import nekomp.core.resources.generated.resources.library_detail_rating_sheet_status_lt_7
import nekomp.core.resources.generated.resources.library_detail_rating_sheet_status_lt_8
import nekomp.core.resources.generated.resources.library_detail_rating_sheet_status_lt_9
import nekomp.core.resources.generated.resources.library_detail_rating_sheet_status_lt_95
import nekomp.core.resources.generated.resources.library_detail_rating_sheet_status_no_rating
import nekomp.core.resources.generated.resources.library_detail_rating_sheet_status_rating
import nekomp.core.resources.generated.resources.library_detail_rating_sheet_status_subtitle
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

                RatingDisplay(ratingValue = dirtyRating)
                RatingSlider(
                    ratingValue = dirtyRating,
                    onRatingChange = {
                        dirtyRating = it.roundToInt()
                    }
                )

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

@Composable
private fun RatingDisplay(ratingValue: Int) {
    val rating = ratingValue.toFloat() / 2
    val ratingDescription = stringResource(
        when {
            rating == 0f -> NekoRes.string.library_detail_rating_sheet_status_0
            rating < 3f -> NekoRes.string.library_detail_rating_sheet_status_lt_3
            rating < 5f -> NekoRes.string.library_detail_rating_sheet_status_lt_5
            rating < 6f -> NekoRes.string.library_detail_rating_sheet_status_lt_6
            rating < 7f -> NekoRes.string.library_detail_rating_sheet_status_lt_7
            rating < 8f -> NekoRes.string.library_detail_rating_sheet_status_lt_8
            rating < 9f -> NekoRes.string.library_detail_rating_sheet_status_lt_9
            rating < 9.5f -> NekoRes.string.library_detail_rating_sheet_status_lt_95
            rating < 10f -> NekoRes.string.library_detail_rating_sheet_status_lt_10
            else -> NekoRes.string.library_detail_rating_sheet_status_10
        }
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Surface(
            modifier = Modifier.size(120.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            tonalElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (rating > 0f) Icons.Filled.Star else Icons.Outlined.StarBorder,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = if (rating > 0f) rating.toString() else "0.0",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    text = stringResource(NekoRes.string.library_detail_rating_sheet_status_rating),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        Text(
            text = ratingDescription,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun RatingSlider(
    ratingValue: Int,
    onRatingChange: (Float) -> Unit
) {
    val rating = ratingValue.toFloat()

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = stringResource(NekoRes.string.library_detail_rating_sheet_status_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Slider(
            value = rating,
            onValueChange = onRatingChange,
            valueRange = 0f..20f,
            steps = 19,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "0",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "5",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "10",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
