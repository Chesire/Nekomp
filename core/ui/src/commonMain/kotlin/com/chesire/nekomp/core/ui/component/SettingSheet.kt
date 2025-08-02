@file:OptIn(ExperimentalMaterial3Api::class)

package com.chesire.nekomp.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableMap

@Composable
fun <T : Enum<T>> SettingSheet(
    sheetState: SheetState,
    title: String,
    entries: ImmutableMap<T, String>,
    selectedEntry: T,
    execute: (T?) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { execute(null) },
        content = {
            Column(
                modifier = Modifier.padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    modifier = Modifier.padding(16.dp)
                )
                entries.forEach { entry ->
                    Row(
                        modifier = Modifier
                            .clickable(
                                enabled = true,
                                onClick = { execute(entry.key) }
                            )
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = entry.value,
                            modifier = Modifier.weight(1f)
                        )
                        RadioButton(
                            selected = entry == selectedEntry,
                            onClick = null
                        )
                    }
                }
            }
        },
        sheetState = sheetState
    )
}
