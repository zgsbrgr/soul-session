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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.zgsbrgr.soulsession.core.model.data.Episode
import com.zgsbrgr.soulsession.core.model.data.Topic
import com.zgsbrgr.soulsession.core.model.data.previewEpisode
import com.zgsbrgr.soulsession.core.model.data.topicPreview
import com.zgsbrgr.soulsession.core.ui.common.FavoriteButton
import com.zgsbrgr.soulsession.core.ui.common.NavigateUpButton
import com.zgsbrgr.soulsession.core.ui.component.SoulSessionBackground
import com.zgsbrgr.soulsession.core.ui.theme.SoulSessionTheme

/* ktlint-disable max-line-length */

@Composable
fun EpisodeRoute(
    modifier: Modifier = Modifier,
    viewModel: EpisodeViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    EpisodeScreen(
        uiState = uiState.episodeState,
        modifier = modifier,
        viewModel = viewModel,
        onNavigateUpClick = onBackClick,
        onPodcastPlayClick = {
            viewModel.selectTopic(it)
        }
    )
}

@Composable
fun EpisodeScreen(
    uiState: EpisodeUiState,
    modifier: Modifier,
    viewModel: EpisodeViewModel = hiltViewModel(),
    onNavigateUpClick: () -> Unit,
    onPodcastPlayClick: (Topic?) -> Unit
) {

    val scrollState = rememberScrollState()

    Column(modifier = modifier.statusBarsPadding()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NavigateUpButton(
                onClick = onNavigateUpClick
            )
            FavoriteButton(
                isFavorite = viewModel.favourited,
                modifier = Modifier.padding(top = 8.dp),
                onClick = {
                    viewModel.favorite()
                }
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
                    val episode = uiState.episode
                    val topic = uiState.episode.topic

                    EpisodeCover(topic.imageUrl, modifier)
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = uiState.episode.title,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.alpha(0.8f)
                    )

                    topic.podcastUrl?.let { _ ->
                        Spacer(modifier = Modifier.height(20.dp))
                        PodcastPlayButton(
                            podcastTopic = topic,
                            modifier = modifier,
                            viewModel = viewModel,
                            onPodcastPlayClick = onPodcastPlayClick
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Information(episode = episode, modifier = modifier)
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
    viewModel: EpisodeViewModel,
    onPodcastPlayClick: (Topic?) -> Unit
) {

    val topic = viewModel.selectedPodcast.collectAsState()
    val (buttonText, buttonIcon) = if ((topic.value as Topic?) != null && (topic.value as Topic).id == podcastTopic.id) {
        ("pause" to Icons.Default.Clear)
    } else ("play" to Icons.Default.PlayArrow)

    OutlinedButton(
        onClick = {
            if ((topic.value as Topic?)?.id == podcastTopic.id)
                onPodcastPlayClick(null)
            else
                onPodcastPlayClick(podcastTopic)
        },
        modifier = modifier
            // .width(120.dp)
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(2.dp, Color(0XFF0F9D58)),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
    ) {
        Icon(
            buttonIcon,
            contentDescription = "Play",
            tint = Color(0XFF0F9D58)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = buttonText.uppercase(),
            style = MaterialTheme.typography.bodyMedium.copy(Color(0XFF0F9D58))
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
            painter = painterResource(com.zgsbrgr.soulsession.core.ui.R.drawable.ss),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        )
    } else
        AsyncImage(
            modifier = modifier
                .fillMaxWidth()
                .height(350.dp),
            model = url,
            contentDescription = "Topic image",
            contentScale = ContentScale.Crop
        )
}

@Composable
fun Information(
    episode: Episode,
    modifier: Modifier
) {

    val topic = episode.topic
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
                Text(text = episode.date + " " + time, style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center)
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
                Text(text = location, style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center)
            }
        }
        Spacer(modifier = modifier.height(10.dp))
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            // horizontalArrangement  =  Arrangement.SpaceBetween
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
                Text(text = email, style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center)
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
                Text(text = phone, style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun Topic(topic: Topic, modifier: Modifier) {

    Text(text = topic.title, style = MaterialTheme.typography.bodyLarge, modifier = modifier)
    Spacer(modifier = modifier.height(16.dp))
    if (topic.description != null) {
        Text(
            text = topic.description!!,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.alpha(0.8f)
        )
        Spacer(modifier = modifier.height(16.dp))
    }
}

@Preview
@Composable
fun InformationPreview() {
    Information(episode = previewEpisode, modifier = Modifier)
}

@Preview
@Composable
fun TopicPreview() {
    SoulSessionTheme {
        SoulSessionBackground {
            Topic(
                topic = topicPreview,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview
@Composable
fun EpisodeScreenPreview() {
    SoulSessionTheme {
        SoulSessionBackground {
            EpisodeScreen(
                uiState = EpisodeUiState.Success(previewEpisode),
                modifier = Modifier,
                onNavigateUpClick = { },
                onPodcastPlayClick = { }
            )
        }
    }
}
