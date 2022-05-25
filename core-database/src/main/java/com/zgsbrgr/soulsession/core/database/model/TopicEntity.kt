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

package com.zgsbrgr.soulsession.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.zgsbrgr.soulsession.core.model.data.Topic

@Entity(
    tableName = "topics",
    foreignKeys = [
        ForeignKey(
            entity = EpisodeEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("episodeId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TopicEntity(
    @PrimaryKey
    val id: String,
    val episodeId: String,
    val title: String,
    val description: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    @ColumnInfo(name = "start_time")
    val startTime: String,
    val location: String,
    val presenter: String,
    @ColumnInfo(name = "live_url")
    val liveUrl: String,
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,
    val email: String,
    @ColumnInfo(name = "podcast_url")
    val podcastUrl: String
)

fun TopicEntity.asExternalModel() = Topic(
    id = id,
    title = title,
    episodeId = episodeId,
    description = description,
    imageUrl = imageUrl,
    startTime = startTime,
    location = location,
    presenter = presenter,
    liveUrl = liveUrl,
    phoneNumber = phoneNumber,
    email = email,
    podcastUrl = podcastUrl
)
