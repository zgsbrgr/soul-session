/*
 * Copyright 2022 zgsbrgr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zgsbrgr.soulsession.feature.episodes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.zgsbrgr.soulsession.core.model.data.Episode
import com.zgsbrgr.soulsession.core.ui.common.StaggeredVerticalGrid
import com.zgsbrgr.soulsession.core.ui.theme.Red40
import com.zgsbrgr.soulsession.core.ui.theme.SoulSessionTypo

@Composable
fun EpisodesRoute(
    modifier: Modifier = Modifier,
    navigateToEpisode: (String) -> Unit,
    viewModel: EpisodesViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    EpisodesScreen(
        uiState = uiState.episodesState,
        navigateToEpisode = navigateToEpisode,
        modifier = modifier
    )
}

@Composable
fun EpisodesScreen(
    uiState: EpisodesUiState,
    navigateToEpisode: (String) -> Unit,
    modifier: Modifier
) {

    val scrollState = rememberLazyListState()

    LazyColumn(state = scrollState, modifier = modifier.statusBarsPadding()) {
        item {
            Box(
                modifier = Modifier.height(100.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "SoulSession archive",
                    style = SoulSessionTypo.headlineLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        when (uiState) {
            is EpisodesUiState.Success -> {
                item {

                    StaggeredVerticalGrid(
                        columnCount = 2,
                        spacing = 16.dp,
                        modifier = Modifier.padding(horizontal = 16.dp)

                    ) {
                        uiState.episodes.forEach { episode ->
                            EpisodeItem(
                                episode = episode,
                                modifier = Modifier.padding(bottom = 16.dp),
                                onClick = {
                                    navigateToEpisode(episode.id)
                                }

                            )
                        }
                    }
                }
            }
            is EpisodesUiState.Loading -> {
                // TODO
                item {
                    Text(text = "page is loading")
                }
            }
            is EpisodesUiState.Error -> {
                // TODO
                item {
                    Text(text = "some error occured")
                }
            }
        }
    }
}

@Composable
fun EpisodeItem(
    episode: Episode,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.background)
            .clickable(onClick = onClick)
    ) {
        EpisodeImage(
            url = episode.thumbnail,
            aspectRatio = 1f
        )
        Text(
            text = episode.title,
            style = SoulSessionTypo.labelMedium,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Red40.copy(alpha = 0.8f))
                .padding(8.dp),
            maxLines = 2
        )
    }
}

@Composable
fun EpisodeImage(
    url: String,
    modifier: Modifier = Modifier,
    aspectRatio: Float = 1f
) {

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .aspectRatio(aspectRatio)
            .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
    ) {
        if (url.isNotEmpty()) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = url,
                contentDescription = "Episode thumbnail image",
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painterResource(com.zgsbrgr.soulsession.core.ui.R.drawable.ss),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
