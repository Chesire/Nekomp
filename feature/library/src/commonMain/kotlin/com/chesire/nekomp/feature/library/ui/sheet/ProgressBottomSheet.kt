@file:OptIn(ExperimentalMaterial3Api::class)

package com.chesire.nekomp.feature.library.ui.sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.core.resources.NekoRes
import nekomp.core.resources.generated.resources.library_detail_progress_sheet_anime_label
import nekomp.core.resources.generated.resources.library_detail_progress_sheet_anime_subtitle
import nekomp.core.resources.generated.resources.library_detail_progress_sheet_cancel_cta
import nekomp.core.resources.generated.resources.library_detail_progress_sheet_manga_label
import nekomp.core.resources.generated.resources.library_detail_progress_sheet_manga_subtitle
import nekomp.core.resources.generated.resources.library_detail_progress_sheet_title
import nekomp.core.resources.generated.resources.library_detail_progress_sheet_update_cta
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ProgressBottomSheet(
    sheetState: SheetState,
    initialProgress: Int,
    maxProgress: Int?,
    seriesTitle: String,
    seriesType: Type,
    execute: (String?) -> Unit
) {
    var dirtyProgress by remember { mutableStateOf(initialProgress.toString()) }
    val digitPattern = remember { Regex("^\\d*$") }
    val progressStart = if (maxProgress != null) {
        "$initialProgress / $maxProgress"
    } else {
        "$initialProgress"
    }
    val progressEnd = if (seriesType == Type.Anime) {
        pluralStringResource(
            NekoRes.plurals.library_detail_progress_sheet_anime_subtitle,
            maxProgress ?: 0
        )
    } else {
        pluralStringResource(
            NekoRes.plurals.library_detail_progress_sheet_manga_subtitle,
            maxProgress ?: 0
        )
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
                    text = stringResource(NekoRes.string.library_detail_progress_sheet_title),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = seriesTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "$progressStart $progressEnd",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                OutlinedTextField(
                    value = dirtyProgress,
                    onValueChange = { newValue ->
                        val isValid = newValue.count() <= 5 && newValue.matches(digitPattern)
                        val isWithinMax = maxProgress?.let { max ->
                            (newValue.toIntOrNull() ?: 0) <= max
                        } ?: true

                        if (isValid && isWithinMax) {
                            dirtyProgress = newValue
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = if (seriesType == Type.Anime) {
                                stringResource(NekoRes.string.library_detail_progress_sheet_anime_label)
                            } else {
                                stringResource(NekoRes.string.library_detail_progress_sheet_manga_label)
                            }
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(
                        onAny = {
                            execute(dirtyProgress)
                        }
                    )
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
                        Text(stringResource(NekoRes.string.library_detail_progress_sheet_cancel_cta))
                    }

                    Button(
                        onClick = { execute(dirtyProgress) },
                        //enabled = !isError && progressText.isNotEmpty() && progressText.toIntOrNull() != null,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(stringResource(NekoRes.string.library_detail_progress_sheet_update_cta))
                    }
                }
            }
        }
    )
    // update dirty progress
    // show bottom sheet with dirty progress to the maxProgress
    // when clicking save - query the viewmodel on if it can be saved, return an error if not (back through the bottom sheet event)
    // if successful, show loading, if not then show error on ui
    // on success of request hide the sheet again
}
