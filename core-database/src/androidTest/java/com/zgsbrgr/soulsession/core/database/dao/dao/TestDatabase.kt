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

package com.zgsbrgr.soulsession.core.database.dao.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.zgsbrgr.soulsession.core.database.SoulSessionDatabase
import com.zgsbrgr.soulsession.core.database.dao.EpisodeDao
import com.zgsbrgr.soulsession.core.database.dao.TopicDao
import com.zgsbrgr.soulsession.core.database.model.EpisodeEntity
import com.zgsbrgr.soulsession.core.database.model.TopicEntity
import org.junit.Before

abstract class TestDatabase {

    lateinit var db: SoulSessionDatabase
    lateinit var episodeDao: EpisodeDao
    lateinit var topicDao: TopicDao

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            SoulSessionDatabase::class.java
        ).build()
        episodeDao = db.episodeDao()
        topicDao = db.topicDao()
    }
}

fun testTopics() = listOf(
    testTopic(
        id = "0",
        episodeId = "0"
    ),
    testTopic(
        id = "1",
        episodeId = "1"
    ),
    testTopic(
        id = "2",
        episodeId = "2"
    )
)

fun testTopic(
    id: String = "0",
    episodeId: String,
    title: String = ""
) = TopicEntity(
    id = id,
    episodeId = episodeId,
    title = title,
    description = "",
    subtitle = "",
    imageUrl = "",
    startTime = "",
    location = "",
    presenter = "",
    liveUrl = "",
    phoneNumber = "",
    email = "",
    podcastUrl = ""
)

fun testEpisodes() = listOf(
    testEpisode(
        id = "0"
    ),
    testEpisode(
        id = "1"
    ),
    testEpisode(
        id = "2"
    )
)

fun testEpisode(
    id: String = "0",
    title: String = ""
) = EpisodeEntity(
    id = id,
    thumbnail = "",
    title = title,
    date = ""
)

fun EpisodeEntity.topicEntityShell() = TopicEntity(
    id = id,
    episodeId = id,
    title = "",
    description = "",
    subtitle = "",
    imageUrl = "",
    startTime = "",
    location = "",
    presenter = "",
    liveUrl = "",
    phoneNumber = "",
    email = "",
    podcastUrl = ""
)
