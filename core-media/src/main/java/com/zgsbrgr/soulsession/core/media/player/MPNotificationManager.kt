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

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.zgsbrgr.soulsession.core.media.Constant
import com.zgsbrgr.soulsession.core.media.R

class MPNotificationManager(
    private val context: Context,
    sessionToken: MediaSessionCompat.Token,
    notificationListener: PlayerNotificationManager.NotificationListener,
    private val newItemCallback: () -> Unit
) {

    private val notificationManager: PlayerNotificationManager

    init {
        val mediaController = MediaControllerCompat(context, sessionToken)
        notificationManager =
            createNotificationManger(mediaController, sessionToken, notificationListener)
    }

    fun showNotification(player: Player) {
        notificationManager.setPlayer(player)
    }

    private fun createNotificationManger(
        mediaController: MediaControllerCompat,
        sessionToken: MediaSessionCompat.Token,
        notificationListener: PlayerNotificationManager.NotificationListener
    ): PlayerNotificationManager {

        return PlayerNotificationManager.Builder(
            context,
            Constant.PLAYBACK_NOTIFICATION_ID,
            Constant.CHANNEL_ID,
            DescriptionAdapter(mediaController)
        )
            .setChannelDescriptionResourceId(R.string.channel_description)
            .setNotificationListener(notificationListener)
            .build()
            .apply {
                // setSmallIcon(com.google.android.exoplayer2.ui.R.drawable.exo_icon_fastforward)
                setMediaSessionToken(sessionToken)
                setUseStopAction(true)
                setUseNextActionInCompactView(true)
                setUsePreviousActionInCompactView(true)
            }
    }

    private inner class DescriptionAdapter(
        private val mediaController: MediaControllerCompat
    ) : PlayerNotificationManager.MediaDescriptionAdapter {
        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            return mediaController.sessionActivity
        }

        override fun getCurrentContentText(player: Player): CharSequence {
            return mediaController.metadata.description.subtitle.toString()
        }

        override fun getCurrentContentTitle(player: Player): CharSequence {
            newItemCallback()
            return mediaController.metadata.description.title.toString()
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            val imageLoader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(mediaController.metadata.description.iconUri)
                .target { drawable ->
                    callback.onBitmap((drawable as BitmapDrawable).bitmap)
                }
                .build()
            val disposable = imageLoader.enqueue(request)
            return null
        }
    }
}
