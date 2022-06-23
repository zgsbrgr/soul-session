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

package com.zgsbrgr.soulsession.feature.player

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.zgsbrgr.soulsession.core.model.data.Topic
import kotlin.math.roundToInt

@Composable
fun SmallPlayerScreen(
    modifier: Modifier,
    viewModel: PlayerViewModel = hiltViewModel(),
    onPlaybackStopped: () -> Unit
) {

    val topic = viewModel.currentPlaying.collectAsState()

    AnimatedVisibility(
        visible = (topic.value != null),
        modifier = modifier
    ) {
        topic.value?.let {
            SmallPlayer(
                it,
                viewModel,
                onPlaybackStopped = onPlaybackStopped
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SmallPlayer(
    topic: Topic,
    viewModel: PlayerViewModel,
    onPlaybackStopped: () -> Unit
) {

    val swipeableState = rememberSwipeableState(0)

    val endAnchor = LocalConfiguration.current.screenWidthDp * LocalDensity.current.density
    val anchors = mapOf(
        0f to 0,
        endAnchor to 1
    )

    val iconResId =
        if (viewModel.podcastIsPlaying)
            R.drawable.ic_round_pause
        else R.drawable.ic_round_play_arrow

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.66f) },
                orientation = Orientation.Horizontal
            )
    ) {
        if (swipeableState.currentValue >= 1) {
            LaunchedEffect("key") {
                viewModel.stopPlayback()
                onPlaybackStopped()
            }
        }

        SmallPlayerStatelessContent(
            topic = topic,
            xOffset = swipeableState.offset.value.roundToInt(),
            darkTheme = isSystemInDarkTheme(),
            icon = iconResId,
            onTogglePlaybackState = {
                viewModel.togglePlaybackState()
            }
        ) {
            viewModel.showPlayerFullScreen = true
        }
    }
}

@Composable
fun SmallPlayerStatelessContent(
    topic: Topic,
    xOffset: Int,
    darkTheme: Boolean,
    @DrawableRes icon: Int,
    onTogglePlaybackState: () -> Unit,
    onTap: (Offset) -> Unit,
) {
    Box(
        modifier = Modifier
            .offset { IntOffset(xOffset, 0) }
            // .background(if (darkTheme) Color(0xFF343434) else Color(0xFFF1F1F1))
            // .background(Color(0xFF343434))
            .background(Color(0xFF000000))
            .navigationBarsPadding()
            .height(64.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = onTap
                )
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier.size(64.dp),
                model = topic.imageUrl,
                contentDescription = "Topic podcast image",
                contentScale = ContentScale.Crop
            )

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(8.dp),
            ) {
                Text(
                    topic.title,
                    style = MaterialTheme.typography.body2,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                topic.description?.let { description ->
                    Text(
                        description,
                        style = MaterialTheme.typography.body2,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.graphicsLayer {
                            alpha = 0.60f
                        }
                    )
                }
            }

            Icon(
                painter = painterResource(icon),
                contentDescription = stringResource(R.string.play),
                // tint = MaterialTheme.colors.onBackground,
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onTogglePlaybackState)
                    .padding(6.dp)
            )
        }
    }
}
