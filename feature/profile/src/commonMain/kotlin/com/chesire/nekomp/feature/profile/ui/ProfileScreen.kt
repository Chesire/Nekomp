@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package com.chesire.nekomp.feature.profile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.chesire.nekomp.core.resources.NekoRes
import kotlinx.collections.immutable.ImmutableList
import nekomp.core.resources.generated.resources.nav_content_description_go_back
import nekomp.core.resources.generated.resources.nav_content_description_settings
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    goBack: () -> Unit,
    navigateToSettings: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Render(
        state = state,
        goBack = goBack,
        navigateToSettings = navigateToSettings,
        execute = { viewModel.execute(it) }
    )
}

@Composable
private fun Render(
    state: UIState,
    goBack: () -> Unit,
    navigateToSettings: () -> Unit,
    execute: (ViewAction) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(
                                NekoRes.string.nav_content_description_go_back
                            )
                        )
                    }
                },
                actions = {
                    IconButton(onClick = navigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(NekoRes.string.nav_content_description_settings)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                ),
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            UserBlock(state.user)
            HighlightsBlock(state.highlights)
            BacklogBlock(state.backlog)
            FavoritesBlock(state.favorites)
            // Big stats block, can probably do this later
        }
    }
}

@Composable
private fun UserBlock(userData: UserData) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        AsyncImage(
            model = userData.avatarImage,
            contentDescription = null,
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape),
            placeholder = rememberVectorPainter(Icons.Default.Person),
            error = rememberVectorPainter(Icons.Default.Person)
        )
        Column {
            Text(
                text = userData.name,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = userData.about,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun HighlightsBlock(highlightsData: HighlightsData) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        shape = RoundedCornerShape(8.dp)
    ) {
        HighlightsRow(
            title1 = "Episodes Watched",
            text1 = highlightsData.episodesWatched,
            title2 = "Chapters Read",
            text2 = highlightsData.chaptersRead,
            modifier = Modifier.weight(1f)
        )
        HorizontalDivider()
        HighlightsRow(
            title1 = "Time spent watching",
            text1 = highlightsData.timeSpentWatching,
            title2 = "Series completed",
            text2 = highlightsData.seriesCompleted,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun HighlightsRow(
    title1: String,
    text1: String,
    title2: String,
    text2: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        HighlightsSection(
            title = title1,
            text = text1,
            modifier = Modifier.weight(1f)
        )
        VerticalDivider()
        HighlightsSection(
            title = title2,
            text = text2,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun HighlightsSection(
    title: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .heightIn(min = 120.dp)
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center
        )
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun BacklogBlock(backlogData: BacklogData) {

}

@Composable
private fun FavoritesBlock(favoritesData: FavoritesData) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FavoriteSection("Favorite Character", favoritesData.favoriteCharacters)
        FavoriteSection("Favorite Anime", favoritesData.favoriteAnime)
        FavoriteSection("Favorite Manga", favoritesData.favoriteManga)
    }
}

@Composable
private fun FavoriteSection(title: String, listData: ImmutableList<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 16.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        LazyRow(
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listData) { item ->
                AsyncImage(
                    item,
                    null,
                    modifier = Modifier.clip(RoundedCornerShape(4.dp))
                )
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    val state = UIState(
        user = UserData(
            name = "Nekomp",
            about = "This is a large string to show for the about text"
        ),
        highlights = HighlightsData(
            episodesWatched = "32",
            chaptersRead = "300",
            timeSpentWatching = "3d 21h",
            seriesCompleted = "4"
        )
    )
    Render(
        state = state,
        goBack = {},
        navigateToSettings = {},
        execute = {}
    )
}
