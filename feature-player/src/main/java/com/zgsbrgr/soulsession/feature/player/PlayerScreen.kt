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

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.zgsbrgr.soulsession.core.model.data.Topic
import com.zgsbrgr.soulsession.core.ui.common.IconButton
import kotlin.math.roundToInt

@Composable
fun PlayerScreen(
    modifier: Modifier,
    viewModel: PlayerViewModel = hiltViewModel(),
    backDispatcher: OnBackPressedDispatcher
) {
    val selectedTopic = viewModel.selectedTopic.collectAsState()

    if ((selectedTopic.value as Topic?) != null) {
        val t = (selectedTopic.value as Topic)
        viewModel.playPodcast(t)
    } else
        viewModel.pausePlayback()

    val topic = viewModel.currentPlaying.collectAsState()
    AnimatedVisibility(
        visible = topic.value != null && viewModel.showPlayerFullScreen,
        enter = slideInVertically(
            initialOffsetY = { it }
        ),
        exit = slideOutVertically(
            targetOffsetY = { it }
        )

    ) {
        topic.value?.let {
            PlayerContent(it, viewModel, backDispatcher)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlayerContent(
    topic: Topic,
    viewModel: PlayerViewModel = hiltViewModel(),
    backDispatcher: OnBackPressedDispatcher
) {

    val swipeableState = rememberSwipeableState(0)

    // TODO add status bar height
    val endAnchor = LocalConfiguration.current.screenHeightDp * LocalDensity.current.density + 300

    val backPressedCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.showPlayerFullScreen = false
            }
        }
    }
    val anchors = mapOf(
        0f to 0,
        endAnchor to 1
    )

    val backgroundColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
    var gradientColor by remember {
        mutableStateOf(backgroundColor)
    }
    val imageLoader = ImageLoader(LocalContext.current)
    val request = ImageRequest.Builder(LocalContext.current)
        .data(topic.imageUrl)
        .build()
    LaunchedEffect(key1 = Unit) {
        val result = imageLoader.execute(request)
        if (result is SuccessResult) {
            viewModel.calculateColorPalette(result.drawable) { color ->
                gradientColor = color
            }
        }
    }

    val imagePainter = rememberAsyncImagePainter(model = request)

    val iconResId =
        if (viewModel.podcastIsPlaying) R.drawable.ic_round_pause
        else R.drawable.ic_round_play_arrow

    var sliderIsChanging by remember { mutableStateOf(false) }

    var localSliderValue by remember { mutableStateOf(0f) }

    val sliderProgress = if (sliderIsChanging) localSliderValue
    else viewModel.currentEpisodeProgress

    Box(
        modifier = Modifier
            .fillMaxSize()
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.33f) },
                orientation = Orientation.Vertical
            )
    ) {
        if (swipeableState.currentValue >= 1) {
            LaunchedEffect("key") {
                viewModel.showPlayerFullScreen = false
            }
        }

        PodcastPlayerContent(
            topic = topic,
            darkTheme = isSystemInDarkTheme(),
            imagePainter = imagePainter,
            gradientColor = gradientColor,
            yOffset = swipeableState.offset.value.roundToInt(),
            playPauseIcon = iconResId,
            playbackProgress = sliderProgress,
            currentTime = viewModel.currentPlaybackFormattedPosition,
            totalTime = viewModel.currentEpisodeFormattedDuration,
            onRewind = {
                viewModel.rewind()
            },
            onForward = {
                viewModel.fastForward()
            },
            onTogglePlayback = {
                viewModel.togglePlaybackState()
            },
            onSliderChange = { newPosition ->
                localSliderValue = newPosition
                sliderIsChanging = true
            },
            onSliderChangeFinished = {
                viewModel.seekToFraction(localSliderValue)
                sliderIsChanging = false
            }
        ) {
            viewModel.showPlayerFullScreen = false
        }
    }

    LaunchedEffect("playbackPosition") {
        viewModel.updateCurrentPlaybackPosition()
    }

    DisposableEffect(backDispatcher) {
        backDispatcher.addCallback(backPressedCallback)

        onDispose {
            backPressedCallback.remove()
            viewModel.showPlayerFullScreen = false
        }
    }
}

@Composable
fun PodcastPlayerContent(
    topic: Topic,
    imagePainter: Painter,
    gradientColor: Color,
    yOffset: Int,
    @DrawableRes playPauseIcon: Int,
    playbackProgress: Float,
    currentTime: String,
    totalTime: String,
    darkTheme: Boolean,
    onRewind: () -> Unit,
    onForward: () -> Unit,
    onTogglePlayback: () -> Unit,
    onSliderChange: (Float) -> Unit,
    onSliderChangeFinished: () -> Unit,
    onClose: () -> Unit
) {

    val gradientColors = if (darkTheme) {
        listOf(gradientColor, MaterialTheme.colorScheme.background)
    } else {
        listOf(MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.background)
    }

    val sliderColors = if (darkTheme) {
        SliderDefaults.colors(
            thumbColor = MaterialTheme.colorScheme.onBackground,
            activeTrackColor = MaterialTheme.colorScheme.onBackground,
            inactiveTrackColor = MaterialTheme.colorScheme.onBackground.copy(
                alpha = ProgressIndicatorDefaults.IndicatorBackgroundOpacity
            ),
        )
    } else SliderDefaults.colors(
        thumbColor = gradientColor,
        activeTrackColor = gradientColor,
        inactiveTrackColor = gradientColor.copy(
            alpha = ProgressIndicatorDefaults.IndicatorBackgroundOpacity
        ),
    )

    Box(
        modifier = Modifier
            .offset { IntOffset(0, yOffset) }
            .fillMaxSize()
    ) {
        Surface {
            Box(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            colors = gradientColors,
                            endY = LocalConfiguration.current.screenHeightDp.toFloat() *
                                LocalDensity.current.density
                        )
                    )
                    .fillMaxSize()
                    .systemBarsPadding()
            ) {
                Column {
                    IconButton(
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = stringResource(R.string.close),
                        onClick = onClose
                    )

                    Column(
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {

                        Box(
                            modifier = Modifier
                                .padding(vertical = 32.dp)
                                .clip(MaterialTheme.shapes.medium)
                                .weight(1f, fill = false)
                                .aspectRatio(1f)
                                .background(
                                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f)
                                )
                        ) {
                            Image(
                                painter = imagePainter,
                                contentDescription = stringResource(R.string.thumbnail),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }

                        Text(
                            topic.subtitle ?: topic.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        topic.location?.let {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                it,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onBackground,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.graphicsLayer {
                                    alpha = 0.60f
                                }
                            )
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp)
                        ) {
                            Slider(
                                value = playbackProgress,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                colors = sliderColors,
                                onValueChange = onSliderChange,
                                onValueChangeFinished = onSliderChangeFinished,
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = currentTime,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = totalTime,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_round_replay_10),
                                contentDescription = stringResource(R.string.replay),
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable(onClick = onRewind)
                                    .padding(12.dp)
                                    .size(32.dp)

                            )
                            Icon(
                                painter = painterResource(playPauseIcon),
                                contentDescription = stringResource(R.string.play),
                                tint = MaterialTheme.colorScheme.background,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.onBackground)
                                    .clickable(onClick = onTogglePlayback)
                                    .size(64.dp)
                                    .padding(8.dp)
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_round_forward_10),
                                contentDescription = stringResource(R.string.forward),
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable(onClick = onForward)
                                    .padding(12.dp)
                                    .size(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
