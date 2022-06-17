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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.zgsbrgr.soulsession.core.ui.R
import com.zgsbrgr.soulsession.core.ui.common.NavigateUpButton
import com.zgsbrgr.soulsession.core.ui.theme.SoulSessionTypo

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
        onNavigateUpClick = onBackClick
    )
}

@Composable
fun EpisodeScreen(
    uiState: EpisodeUiState,
    modifier: Modifier,
    onNavigateUpClick: () -> Unit
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
                is EpisodeUiState.Loading -> {}
                is EpisodeUiState.Error -> {}
                is EpisodeUiState.Success -> {
                    val topic = uiState.episode.topic
                    EpisodeCover(topic.imageUrl, modifier)
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(text = uiState.episode.title, style = SoulSessionTypo.titleLarge)
                }
            }
        }
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
            modifier = Modifier.fillMaxWidth().height(200.dp)
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
