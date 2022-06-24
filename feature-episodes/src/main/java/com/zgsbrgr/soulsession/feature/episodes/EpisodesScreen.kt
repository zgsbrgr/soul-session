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

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.zgsbrgr.soulsession.core.model.data.Episode
import com.zgsbrgr.soulsession.core.ui.R
import com.zgsbrgr.soulsession.core.ui.common.StaggeredVerticalGrid
import com.zgsbrgr.soulsession.core.ui.component.SoulSessionBackground
import com.zgsbrgr.soulsession.core.ui.theme.SoulSessionTheme

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

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is EpisodesUiState.Success -> {
                LazyColumn(state = scrollState, modifier = modifier.statusBarsPadding()) {
                    item {
                        Box(
                            modifier = Modifier.height(100.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = "Soul Session Episodes",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
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
            }
            is EpisodesUiState.Loading -> {
                Loader()
            }
            is EpisodesUiState.Error -> {
                Error()
            }
        }
    }
}

@Composable
fun Loader() {
    val infiniteTransition = rememberInfiniteTransition()

    val rotate by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Restart
        )
    )
    Image(
        painterResource(id = R.drawable.sync_fill0_wght400_grad0_opsz48),
        contentDescription = "loading",
        modifier = Modifier
            .width(50.dp)
            .height(50.dp)
            .rotate(rotate)
    )
}

@Composable
fun Error() {
    Image(
        painter = painterResource(id = R.drawable.cloud_off_fill0_wght400_grad0_opsz48),
        contentDescription = "error loading",
        modifier = Modifier
            .width(80.dp)
            .height(80.dp)

    )
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
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .align(Alignment.BottomCenter)
                .background(Color(0xFF000000).copy(alpha = 0.8f))
                .padding(8.dp),
            maxLines = 2,

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
                painterResource(R.drawable.ss),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview
@Composable
fun EpisodesLoadingPreview() {
    SoulSessionTheme {
        SoulSessionBackground {
            EpisodesScreen(
                uiState = EpisodesUiState.Loading,
                navigateToEpisode = { },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
