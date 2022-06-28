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

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v4.media.MediaBrowserCompat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.palette.graphics.Palette
import com.zgsbrgr.soulsession.core.common.events.AppEventBus
import com.zgsbrgr.soulsession.core.media.Constant
import com.zgsbrgr.soulsession.core.media.MPServiceConnection
import com.zgsbrgr.soulsession.core.media.MediaService
import com.zgsbrgr.soulsession.core.media.util.currentPosition
import com.zgsbrgr.soulsession.core.media.util.isPlayEnabled
import com.zgsbrgr.soulsession.core.media.util.isPlaying
import com.zgsbrgr.soulsession.core.model.data.Topic
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val serviceConnection: MPServiceConnection,
    private val appEventBus: AppEventBus
) : ViewModel() {

    val currentPlaying = serviceConnection.currentPlayingTopic

    val selectedTopic = appEventBus.selectedItem

    var showPlayerFullScreen by mutableStateOf(false)

    var currentPlaybackPosition by mutableStateOf(0L)

    val podcastIsPlaying: Boolean
        get() = playbackState.value?.isPlaying == true

    val currentEpisodeProgress: Float
        get() {
            if (currentEpisodeDuration > 0) {
                return currentPlaybackPosition.toFloat() / currentEpisodeDuration
            }
            return 0f
        }

    val currentPlaybackFormattedPosition: String
        get() = formatLong(currentPlaybackPosition)

    val currentEpisodeFormattedDuration: String
        get() = formatLong(currentEpisodeDuration)

    private val playbackState = serviceConnection.playbackState

    private val currentEpisodeDuration: Long
        get() = MediaService.currentDuration

    fun stopPlayback() {
        appEventBus.postMessage(null)
        serviceConnection.transportControls.stop()
    }

    fun togglePlaybackState() {
        when {
            playbackState.value?.isPlaying == true -> {
                serviceConnection.transportControls.pause()
                appEventBus.postMessage(null)
            }

            playbackState.value?.isPlayEnabled == true -> {
                serviceConnection.transportControls.play()
                appEventBus.postMessage(currentPlaying.value)
            }
        }
    }

    fun playPodcast(podcastTopic: Topic) {
        serviceConnection.playPodcast(listOf(podcastTopic))
        if (currentPlaying.value?.id == podcastTopic.id) {
            if (!podcastIsPlaying)
                serviceConnection.transportControls.play()
        } else
            serviceConnection.transportControls.playFromMediaId(podcastTopic.id, null)
    }

    fun pausePlayback() {
        if (podcastIsPlaying)
            serviceConnection.transportControls.pause()
    }

    fun fastForward() {
        serviceConnection.fastForward()
    }

    fun rewind() {
        serviceConnection.rewind()
    }

    /**
     * @param value 0.0 to 1.0
     */
    fun seekToFraction(value: Float) {
        serviceConnection.transportControls.seekTo(
            (currentEpisodeDuration * value).toLong()
        )
    }

    suspend fun updateCurrentPlaybackPosition() {
        val currentPosition = playbackState.value?.currentPosition
        if (currentPosition != null && currentPosition != currentPlaybackPosition) {
            currentPlaybackPosition = currentPosition
        }
        delay(Constant.PLAYBACK_POSITION_UPDATE_INTERVAL)
        updateCurrentPlaybackPosition()
    }

    private fun formatLong(value: Long): String {
        val seconds = value / 1000
        val hh: Long = seconds / 3600
        val mm: Long = seconds % 3600 / 60
        val ss: Long = seconds % 60
        return String.format("%02d:%02d:%02d", hh, mm, ss)
    }

    fun calculateColorPalette(drawable: Drawable, onFinished: (Color) -> Unit) {
        val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bitmap).generate { palette ->
            palette?.darkVibrantSwatch?.rgb?.let { colorValue ->
                onFinished(Color(colorValue))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        serviceConnection.unsubscribe(
            Constant.MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {}
        )
    }
}
