package com.chesire.nekomp.feature.library.data

import com.chesire.nekomp.core.model.EntryStatus
import com.chesire.nekomp.core.resources.NekoRes
import nekomp.core.resources.generated.resources.library_detail_status_sheet_status_completed_description
import nekomp.core.resources.generated.resources.library_detail_status_sheet_status_completed_title
import nekomp.core.resources.generated.resources.library_detail_status_sheet_status_current_description
import nekomp.core.resources.generated.resources.library_detail_status_sheet_status_current_title
import nekomp.core.resources.generated.resources.library_detail_status_sheet_status_dropped_description
import nekomp.core.resources.generated.resources.library_detail_status_sheet_status_dropped_title
import nekomp.core.resources.generated.resources.library_detail_status_sheet_status_on_hold_description
import nekomp.core.resources.generated.resources.library_detail_status_sheet_status_on_hold_title
import nekomp.core.resources.generated.resources.library_detail_status_sheet_status_planned_description
import nekomp.core.resources.generated.resources.library_detail_status_sheet_status_planned_title
import org.jetbrains.compose.resources.StringResource

val EntryStatus.title: StringResource
    get() {
        return when (this) {
            EntryStatus.Current -> NekoRes.string.library_detail_status_sheet_status_current_title
            EntryStatus.OnHold -> NekoRes.string.library_detail_status_sheet_status_on_hold_title
            EntryStatus.Planned -> NekoRes.string.library_detail_status_sheet_status_planned_title
            EntryStatus.Completed -> NekoRes.string.library_detail_status_sheet_status_completed_title
            EntryStatus.Dropped -> NekoRes.string.library_detail_status_sheet_status_dropped_title
        }
    }

val EntryStatus.description: StringResource
    get() {
        return when (this) {
            EntryStatus.Current -> NekoRes.string.library_detail_status_sheet_status_current_description
            EntryStatus.OnHold -> NekoRes.string.library_detail_status_sheet_status_on_hold_description
            EntryStatus.Planned -> NekoRes.string.library_detail_status_sheet_status_planned_description
            EntryStatus.Completed -> NekoRes.string.library_detail_status_sheet_status_completed_description
            EntryStatus.Dropped -> NekoRes.string.library_detail_status_sheet_status_dropped_description
        }
    }
