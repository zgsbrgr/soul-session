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

package com.zgsbrgr.soulsession.core.media.player

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.zgsbrgr.soulsession.core.model.data.Topic
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PodcastMediaSource @Inject constructor() {

    var mediaMetadataTopics: List<MediaMetadataCompat> = emptyList()
    var podcastsFromTopic: List<Topic> = emptyList()
        private set

    private val readyListeners = mutableListOf<ReadyListeners>()

    private val isReady: Boolean
        get() = state == SourceState.INITIALIZED

    private var state: SourceState =
        SourceState.CREATED
        set(value) {
                if (value == SourceState.INITIALIZED || value == SourceState.INITIALIZING) {
                    synchronized(readyListeners) {
                        field = value
                        readyListeners.forEach { listener ->
                            listener(isReady)
                        }
                    }
                } else
                    field = value
            }

    fun setPodcastTopics(items: List<Topic>) {
        state = SourceState.INITIALIZING
        podcastsFromTopic = items
        mediaMetadataTopics = items.map { topic ->
            MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, topic.id)
                .putString(
                    MediaMetadataCompat.METADATA_KEY_ARTIST, topic.presenter
                )
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, topic.title)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, topic.podcastUrl)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, topic.imageUrl)
                .build()
        }
        state = SourceState.INITIALIZED
    }

    fun asMediaItems() = mediaMetadataTopics.map { metadata ->
        val description = MediaDescriptionCompat.Builder()
            .setMediaId(metadata.description.mediaId)
            .setTitle(metadata.description.title)
            .setSubtitle(metadata.description.subtitle)
            .setIconUri(metadata.description.iconUri)
            .setMediaUri(metadata.description.mediaUri)
            .build()
        MediaBrowserCompat.MediaItem(description, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE)
    }.toMutableList()

    fun asMediaSource(dataSourceFactory: DataSource.Factory): ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        mediaMetadataTopics.forEach { metadata ->
            val mediaItem = MediaItem.fromUri(
                metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI)
            )
            val mediaSource =
                ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return concatenatingMediaSource
    }

    fun whenReady(listener: ReadyListeners): Boolean {
        return if (state == SourceState.CREATED || state == SourceState.INITIALIZING) {
            readyListeners += listener
            false
        } else {
            listener(isReady)
            true
        }
    }

    fun refresh() {
        readyListeners.clear()
        state = SourceState.CREATED
    }
}
typealias ReadyListeners = (Boolean) -> Unit

enum class SourceState {
    CREATED,
    INITIALIZING,
    INITIALIZED,
    ERROR
}
