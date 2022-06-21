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

package com.zgsbrgr.soulsession.core.media.util

import android.os.SystemClock
import android.support.v4.media.session.PlaybackStateCompat

inline val PlaybackStateCompat.isPrepared: Boolean
    get() = state == PlaybackStateCompat.STATE_BUFFERING ||
        state == PlaybackStateCompat.STATE_PLAYING ||
        state == PlaybackStateCompat.STATE_PAUSED

inline val PlaybackStateCompat.isPlaying: Boolean
    get() = state == PlaybackStateCompat.STATE_BUFFERING ||
        state == PlaybackStateCompat.STATE_PLAYING

inline val PlaybackStateCompat.isPlayEnabled: Boolean
    get() = actions and PlaybackStateCompat.ACTION_PLAY != 0L ||
        (
            actions and PlaybackStateCompat.ACTION_PLAY_PAUSE != 0L &&
                state == PlaybackStateCompat.STATE_PAUSED
            )

inline val PlaybackStateCompat.isStopped: Boolean
    get() = state == PlaybackStateCompat.STATE_NONE ||
        state == PlaybackStateCompat.STATE_ERROR

inline val PlaybackStateCompat.isError: Boolean
    get() = state == PlaybackStateCompat.STATE_ERROR

inline val PlaybackStateCompat.currentPosition: Long
    get() = if (state == PlaybackStateCompat.STATE_PLAYING) {
        val timeDelta = SystemClock.elapsedRealtime() - lastPositionUpdateTime
        (position + (timeDelta * playbackSpeed)).toLong()
    } else position
