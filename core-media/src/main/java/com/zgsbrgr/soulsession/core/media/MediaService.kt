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

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.zgsbrgr.soulsession.core.media.player.MPNotificationListener
import com.zgsbrgr.soulsession.core.media.player.MPNotificationManager
import com.zgsbrgr.soulsession.core.media.player.MPPreparer
import com.zgsbrgr.soulsession.core.media.player.MPQueueNavigator
import com.zgsbrgr.soulsession.core.media.player.PodcastMediaSource
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

@AndroidEntryPoint
class MediaService : MediaBrowserServiceCompat() {

    @Inject
    lateinit var dataSourceFactory: CacheDataSource.Factory

    @Inject
    lateinit var player: ExoPlayer

    @Inject
    lateinit var mediaSource: PodcastMediaSource

    @Inject
    @ApplicationContext
    lateinit var context: Context

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    private lateinit var notificationManager: MPNotificationManager

    private var currentlyPlaying: MediaMetadataCompat? = null

    private var isPlayerInitialized = false

    var isForegroundService: Boolean = false

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate called")
        val pendingFlags = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            -> PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            else -> PendingIntent.FLAG_UPDATE_CURRENT
        }

        val activityPendingIntent = Intent(this, context::class.java)
            .apply {
                action = Constant.PODCAST_NOTIFICATION_CLICK
            }
            .let {
                PendingIntent.getActivity(
                    this,
                    0,
                    it,
                    pendingFlags
                )
            }

        mediaSession = MediaSessionCompat(this, TAG).apply {
            setSessionActivity(activityPendingIntent)
            isActive = true
        }

        val mediaPlaybackPreparer = MPPreparer(mediaSource) { mediaMetadata ->
            currentlyPlaying = mediaMetadata
            preparePlayer(mediaSource.mediaMetadataTopics, mediaMetadata, true)
        }
        mediaSessionConnector = MediaSessionConnector(mediaSession).apply {
            setPlaybackPreparer(mediaPlaybackPreparer)
            setQueueNavigator(MPQueueNavigator(mediaSession, mediaSource))
            setPlayer(player)
        }

        this.sessionToken = mediaSession.sessionToken

        notificationManager = MPNotificationManager(
            this,
            mediaSession.sessionToken,
            MPNotificationListener(this)
        ) {
            currentDuration = player.duration
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    override fun onCustomAction(action: String, extras: Bundle?, result: Result<Bundle>) {
        super.onCustomAction(action, extras, result)
        when (action) {
            Constant.START_MEDIA_PLAYBACK -> {
                notificationManager.showNotification(player)
            }
            Constant.REFRESH_MEDIA_BROWSER_CHILDREN -> {
                mediaSource.refresh()
                notifyChildrenChanged(Constant.MEDIA_ROOT_ID)
            }
            else -> Unit
        }
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot(Constant.MEDIA_ROOT_ID, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        Log.i(TAG, "onLoadChildren called")
        when (parentId) {
            Constant.MEDIA_ROOT_ID -> {
                val resultsSent = mediaSource.whenReady { isInitialized ->
                    if (isInitialized) {

                        result.sendResult(mediaSource.asMediaItems())
                        if (!isPlayerInitialized && mediaSource.mediaMetadataTopics.isNotEmpty()) {
                            isPlayerInitialized = true
                        }
                    } else {
                        result.sendResult(null)
                    }
                }
                if (!resultsSent) {
                    result.detach()
                }
            }
            else -> Unit
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        player.release()
    }

    private fun preparePlayer(
        mediaMetaData: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playWhenReady: Boolean
    ) {
        val indexToPlay = if (currentlyPlaying == null) 0 else mediaMetaData.indexOf(itemToPlay)
        player.setMediaSource(mediaSource.asMediaSource(dataSourceFactory))
        player.prepare()
        player.seekTo(indexToPlay, 0L)
        player.playWhenReady = playWhenReady
    }

    companion object {
        private const val TAG = "MediaService"
        var currentDuration: Long = 0L
            private set
    }
}
