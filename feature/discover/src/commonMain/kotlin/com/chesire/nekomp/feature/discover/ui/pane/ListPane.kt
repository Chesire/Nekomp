@file:OptIn(ExperimentalComposeUiApi::class)

package com.chesire.nekomp.feature.discover.ui.pane

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.chesire.nekomp.feature.discover.ui.DiscoverItem
import com.chesire.nekomp.feature.discover.ui.ResultsState
import com.chesire.nekomp.feature.discover.ui.TrendingState
import com.chesire.nekomp.feature.discover.ui.ViewAction
import kotlinx.collections.immutable.ImmutableList

internal enum class ListPaneType {
    Trending,
    Search,
    Results
}

@Suppress("LongMethod", "LongParameterList")
@Composable
internal fun ListPane(
    listPaneType: ListPaneType,
    searchText: String,
    recentSearches: ImmutableList<String>,
    trendingState: TrendingState,
    resultsState: ResultsState,
    execute: (ViewAction) -> Unit,
    onItemClick: (DiscoverItem) -> Unit,
    updateListPaneType: (ListPaneType) -> Unit
) {
    val focus = LocalFocusManager.current
    BackHandler(enabled = listPaneType != ListPaneType.Trending) {
        when (listPaneType) {
            ListPaneType.Trending -> Unit
            ListPaneType.Search -> {
                updateListPaneType(ListPaneType.Trending)
                focus.clearFocus(true)
            }

            ListPaneType.Results -> {
                updateListPaneType(ListPaneType.Search)
            }
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row {
            AnimatedVisibility(
                visible = listPaneType != ListPaneType.Trending,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                IconButton(
                    onClick = {
                        when (listPaneType) {
                            ListPaneType.Trending -> Unit
                            ListPaneType.Search -> {
                                updateListPaneType(ListPaneType.Trending)
                                focus.clearFocus(true)
                            }

                            ListPaneType.Results -> {
                                updateListPaneType(ListPaneType.Search)
                            }
                        }
                        focus.clearFocus(true)
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
            OutlinedTextField(
                value = searchText,
                onValueChange = { execute(ViewAction.SearchTextUpdated(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .animateContentSize()
                    .onFocusEvent {
                        if (it.hasFocus) {
                            updateListPaneType(ListPaneType.Search)
                        }
                    },
                label = {
                    Text("Search")
                },
                leadingIcon = if (listPaneType == ListPaneType.Trending) {
                    { Icon(imageVector = Icons.Default.Search, contentDescription = null) }
                } else {
                    null
                },
                trailingIcon = {
                    AnimatedVisibility(searchText.isNotEmpty()) {
                        IconButton(
                            onClick = { execute(ViewAction.SearchTextUpdated("")) }
                        ) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                        }
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focus.clearFocus(true)
                        execute(ViewAction.SearchExecute)
                        // TODO: Find better way to show this once the search is done. Probably VE?
                        updateListPaneType(ListPaneType.Results)
                    }
                ),
                singleLine = true
            )
        }
        when (listPaneType) {
            ListPaneType.Trending -> TrendingPane(
                trendingState = trendingState,
                onItemClick = onItemClick
            )

            ListPaneType.Search -> SearchPane(
                recentSearches = recentSearches,
                onRecentClicked = { execute(ViewAction.RecentSearchClick(it)) }
            )

            ListPaneType.Results -> ResultsPane(
                resultsState = resultsState,
                onItemClick = onItemClick
            )
        }
    }
}
