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

package com.zgsbrgr.soulsession.core.media

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.runtime.mutableStateOf
import com.zgsbrgr.soulsession.core.media.player.PodcastMediaSource
import com.zgsbrgr.soulsession.core.media.util.currentPosition
import com.zgsbrgr.soulsession.core.model.data.Topic

class MPServiceConnection(
    context: Context,
    private val podcastMediaSource: PodcastMediaSource
) {

    var playbackState = mutableStateOf<PlaybackStateCompat?>(null)
    var currentPlayingTopic = mutableStateOf<Topic?>(null)

    lateinit var mediaController: MediaControllerCompat

    private var isConnected: Boolean = false

    val transportControls: MediaControllerCompat.TransportControls
        get() = mediaController.transportControls

    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback(context)

    private val mediaBrowser = MediaBrowserCompat(
        context,
        ComponentName(context, MediaService::class.java),
        mediaBrowserConnectionCallback,
        null
    ).apply {
        connect()
    }

    fun playPodcast(topics: List<Topic>) {
        podcastMediaSource.setPodcastTopics(topics)
        mediaBrowser.sendCustomAction(Constant.START_MEDIA_PLAYBACK, null, null)
    }

    fun fastForward(seconds: Int = 10) {
        playbackState.value?.currentPosition?.let { currentPosition ->
            transportControls.seekTo(currentPosition + seconds * 1000)
        }
    }

    fun rewind(seconds: Int = 10) {
        playbackState.value?.currentPosition?.let { currentPosition ->
            transportControls.seekTo(currentPosition - seconds * 1000)
        }
    }

    fun subscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.subscribe(parentId, callback)
    }

    fun unsubscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.unsubscribe(parentId, callback)
    }

    fun refreshMediaBrowserChildren() {
        mediaBrowser
            .sendCustomAction(Constant.REFRESH_MEDIA_BROWSER_CHILDREN, null, null)
    }

    private inner class MediaBrowserConnectionCallback(
        private val context: Context
    ) : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            super.onConnected()
            isConnected = true
            mediaController =
                MediaControllerCompat(context, mediaBrowser.sessionToken).apply {
                    registerCallback(MediaControllerCallback())
                }
        }

        override fun onConnectionSuspended() {
            super.onConnectionSuspended()
            isConnected = false
        }

        override fun onConnectionFailed() {
            super.onConnectionFailed()
            isConnected = false
        }
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            playbackState.value = state
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            currentPlayingTopic.value = metadata?.let {
                podcastMediaSource.podcastsFromTopic.find {
                    it.id == metadata.description?.mediaId
                }
            }
        }

        override fun onSessionDestroyed() {
            super.onSessionDestroyed()
            mediaBrowserConnectionCallback.onConnectionSuspended()
        }
    }
}
