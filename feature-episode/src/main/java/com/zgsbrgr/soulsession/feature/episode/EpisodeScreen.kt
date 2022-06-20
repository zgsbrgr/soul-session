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

package com.zgsbrgr.soulsession.feature.episode

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.zgsbrgr.soulsession.core.model.data.Topic
import com.zgsbrgr.soulsession.core.ui.R
import com.zgsbrgr.soulsession.core.ui.common.NavigateUpButton
import com.zgsbrgr.soulsession.core.ui.theme.SoulSessionTypo

/* ktlint-disable max-line-length */

@Composable
fun EpisodeRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EpisodeViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    EpisodeScreen(
        uiState = uiState.episodeState,
        modifier = modifier,
        onNavigateUpClick = onBackClick,
        onPodcastPlayClick = viewModel::playPodcast
    )
}

@Composable
fun EpisodeScreen(
    uiState: EpisodeUiState,
    modifier: Modifier,
    onNavigateUpClick: () -> Unit,
    onPodcastPlayClick: (Topic) -> Unit
) {

    val scrollState = rememberScrollState()

    Column(modifier = Modifier.statusBarsPadding()) {
        Row {
            NavigateUpButton(
                onClick = onNavigateUpClick
            )
        }
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .navigationBarsPadding()
                .padding(vertical = 24.dp, horizontal = 16.dp)
                .padding(bottom = 64.dp)
        ) {
            when (uiState) {
                is EpisodeUiState.Loading -> {
                    // TODO add loading screen
                }
                is EpisodeUiState.Error -> {
                    // TODO add error screen
                }
                is EpisodeUiState.Success -> {
                    val topic = uiState.episode.topic
                    EpisodeCover(topic.imageUrl, modifier)
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = uiState.episode.title,
                        style = SoulSessionTypo.titleLarge,
                        modifier = Modifier.alpha(0.8f)
                    )

                    topic.podcastUrl?.let { _ ->
                        Spacer(modifier = Modifier.height(20.dp))
                        PodcastPlayButton(
                            podcastTopic = topic,
                            modifier = modifier,
                            onPodcastPlayClick = onPodcastPlayClick
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Information(topic = topic, modifier = modifier)
                    Spacer(modifier = modifier.height(20.dp))
                    Topic(topic = topic, modifier = Modifier.padding(start = 8.dp, end = 8.dp))
                }
            }
        }
    }
}

@Composable
fun PodcastPlayButton(
    podcastTopic: Topic,
    modifier: Modifier,
    onPodcastPlayClick: (Topic) -> Unit
) {
    OutlinedButton(
        onClick = { onPodcastPlayClick(podcastTopic) },
        modifier = modifier
            .width(120.dp)
            .height(50.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(2.dp, Color(0XFF0F9D58)),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
    ) {
        Icon(
            Icons.Default.PlayArrow,
            contentDescription = "Play",
            tint = Color(0XFF0F9D58)
        )
        Text(
            text = "play".uppercase(),
            style = SoulSessionTypo.bodyMedium.copy(Color(0XFF0F9D58))
        )
    }
}

@Composable
fun EpisodeCover(
    url: String?,
    modifier: Modifier = Modifier
) {
    if (url.isNullOrEmpty()) {
        Image(
            painter = painterResource(R.drawable.ss),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    } else
        AsyncImage(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp),
            model = url,
            contentDescription = "Topic image",
            contentScale = ContentScale.Crop
        )
}

@Composable
fun Information(topic: Topic, modifier: Modifier) {
    val scrollState = rememberScrollState()
    Column {
        Row(
            modifier = modifier
                .height(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(
                    id =
                    com.zgsbrgr.soulsession.feature.episode.R.drawable.timer_fill0_wght400_grad0_opsz48
                ),
                contentDescription = "Time",
                modifier = modifier
                    .width(20.dp)
            )
            Spacer(modifier = modifier.width(10.dp))
            topic.startTime?.let { time ->
                Text(text = time, style = SoulSessionTypo.labelSmall, textAlign = TextAlign.Center)
            }
        }
        Spacer(modifier = modifier.height(10.dp))
        Row(modifier = modifier.height(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painterResource(
                    id = com.zgsbrgr.soulsession.feature.episode.R.drawable.home_pin_fill0_wght400_grad0_opsz48
                ),
                contentDescription = "Location",
                modifier = modifier
                    .width(20.dp)
            )
            Spacer(modifier = modifier.width(10.dp))
            topic.location?.let { location ->
                Text(text = location, style = SoulSessionTypo.labelSmall, textAlign = TextAlign.Center)
            }
        }
        Spacer(modifier = modifier.height(10.dp))
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(
                    id = com.zgsbrgr.soulsession.feature.episode.R.drawable.alternate_email_fill0_wght400_grad0_opsz48
                ),
                contentDescription = "Email",
                modifier = modifier
                    .width(20.dp)
            )
            Spacer(modifier = modifier.width(10.dp))
            topic.email?.let { email ->
                Text(text = email, style = SoulSessionTypo.labelSmall, textAlign = TextAlign.Center)
            }
            Spacer(modifier = modifier.width(20.dp))
            Image(
                painterResource(
                    id = com.zgsbrgr.soulsession.feature.episode.R.drawable.call_fill0_wght400_grad0_opsz48
                ),
                contentDescription = "Phone",
                modifier = modifier
                    .width(20.dp)
            )
            Spacer(modifier = modifier.width(10.dp))
            topic.phoneNumber?.let { phone ->
                Text(text = phone, style = SoulSessionTypo.labelSmall, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun Topic(topic: Topic, modifier: Modifier) {

    Text(text = topic.title, style = SoulSessionTypo.bodyLarge, modifier = modifier)
    Spacer(modifier = modifier.height(16.dp))
    if (topic.description != null) {
        Text(
            text = topic.description!!,
            style = SoulSessionTypo.bodyMedium,
            modifier = modifier.alpha(0.8f)
        )
        Spacer(modifier = modifier.height(16.dp))
    }
}
