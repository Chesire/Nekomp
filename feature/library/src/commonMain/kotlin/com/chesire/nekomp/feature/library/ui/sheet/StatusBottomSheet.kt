@file:OptIn(ExperimentalMaterial3Api::class)

package com.chesire.nekomp.feature.library.ui.sheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.chesire.nekomp.core.model.EntryStatus
import com.chesire.nekomp.core.resources.NekoRes
import com.chesire.nekomp.feature.library.data.description
import com.chesire.nekomp.feature.library.data.title
import com.chesire.nekomp.feature.library.ui.LibraryBottomSheet
import kotlinx.collections.immutable.PersistentSet
import nekomp.core.resources.generated.resources.library_detail_sheet_cancel_cta
import nekomp.core.resources.generated.resources.library_detail_sheet_update_cta
import nekomp.core.resources.generated.resources.library_detail_status_sheet_status_current_status
import nekomp.core.resources.generated.resources.library_detail_status_sheet_status_subtitle
import nekomp.core.resources.generated.resources.library_detail_status_sheet_title
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun StatusBottomSheet(
    sheetState: SheetState,
    currentStatus: EntryStatus,
    allStatus: PersistentSet<EntryStatus>,
    seriesTitle: String,
    state: LibraryBottomSheet.BottomSheetState,
    execute: (EntryStatus?) -> Unit
) {
    var dirtyStatus by remember { mutableStateOf(currentStatus) }

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
                    text = stringResource(NekoRes.string.library_detail_status_sheet_title),
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
                        text = stringResource(
                            NekoRes.string.library_detail_status_sheet_status_current_status,
                            currentStatus.name
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column(
                    modifier = Modifier.selectableGroup(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(NekoRes.string.library_detail_status_sheet_status_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    allStatus.forEach { status ->
                        StatusOption(
                            status = status,
                            isSelected = dirtyStatus == status,
                            modifier = Modifier.fillMaxWidth(),
                            onSelected = { dirtyStatus = status }
                        )
                    }
                }

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
                        onClick = { execute(dirtyStatus) },
                        enabled = state !is LibraryBottomSheet.BottomSheetState.Updating && currentStatus != dirtyStatus,
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
private fun StatusOption(
    status: EntryStatus,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onSelected: () -> Unit
) {
    Surface(
        modifier = modifier
            .selectable(
                selected = isSelected,
                onClick = onSelected,
                role = Role.RadioButton
            ),
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        border = if (isSelected) {
            null
        } else {
            BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
            )
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = null
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = stringResource(status.title),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )

                Text(
                    text = stringResource(status.description),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}
