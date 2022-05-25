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
import com.zgsbrgr.soulsession.core.database.model.EpisodeEntity
import com.zgsbrgr.soulsession.core.database.model.EpisodeWithTopic
import com.zgsbrgr.soulsession.core.database.model.TopicEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/* ktlint-disable max-line-length */

class EpisodeDaoTest {

    private lateinit var episodeDao: EpisodeDao
    private lateinit var db: SoulSessionDatabase

    @Before
    fun createDatabase() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            SoulSessionDatabase::class.java
        ).build()
        episodeDao = db.episodeDao()
    }

    @Test
    fun episodeDao_fetches_episodes() = runTest {

        episodeDao.insertEpisodes(testEpisodesWithTopic("", ""))

        val savedEpisodeEntities = episodeDao.getEpisodes().first()

        Assert.assertEquals(
            listOf("0", "1", "2"),
            savedEpisodeEntities.map {
                it.episode.id
            }
        )
    }

    @Test
    fun episodeDao_fetches_topics() = runTest {

        episodeDao.insertEpisodes(testEpisodesWithTopic("", ""))

        val savedEpisodeEntities = episodeDao.getEpisodes().first()

        Assert.assertEquals(
            listOf("0", "1", "2"),
            savedEpisodeEntities.map {
                it.topic.id
            }
        )
    }

    @Test
    fun episodeDao_updates_episode() = runTest {

        episodeDao.insertEpisodes(testEpisodesWithTopic("", ""))

        episodeDao.upsertEpisodeWithTopics(testEpisodesWithTopic("episode title", ""))

        val savedEpisodeEntities = episodeDao.getEpisodes().first()

        Assert.assertEquals(
            listOf("episode title", "episode title", "episode title"),
            savedEpisodeEntities.map {
                it.episode.title
            }
        )
    }

    @Test
    fun episodeDao_updates_topics() = runTest {

        episodeDao.insertEpisodes(testEpisodesWithTopic("", ""))

        episodeDao.upsertEpisodeWithTopics(testEpisodesWithTopic("", "topic title"))

        val savedEpisodeEntities = episodeDao.getEpisodes().first()

        Assert.assertEquals(
            listOf("topic title", "topic title", "topic title"),
            savedEpisodeEntities.map {
                it.topic.title
            }
        )
    }

    @Test
    fun episodeDao_deletes_episodes() = runTest {

        episodeDao.insertEpisodes(testEpisodesWithTopic("", ""))

        episodeDao.deleteEpisodes(listOf("0", "1", "2"))

        val savedEpisodeEntities = episodeDao.getEpisodes().first()

        Assert.assertTrue(savedEpisodeEntities.isEmpty())
    }

    @Test
    fun episodeDao_deletes_episode_and_topics() = runTest {

        episodeDao.insertEpisodes(testEpisodesWithTopic("", ""))

        episodeDao.deleteEpisodes(listOf("0", "1", "2"))

        val savedTopicEntities = episodeDao.getTopics().first()

        Assert.assertTrue(savedTopicEntities.isEmpty())
    }
}

private fun testEpisodesWithTopic(episodeTitle: String, topicTitle: String): List<EpisodeWithTopic> {

    val episodes = listOf(
        testEpisode(id = "0", title = episodeTitle),
        testEpisode(id = "1", title = episodeTitle),
        testEpisode(id = "2", title = episodeTitle)
    )

    val topics = listOf(
        testTopic(
            id = "0",
            episodes[0].id,
            title = topicTitle
        ),
        testTopic(
            id = "1",
            episodes[1].id,
            title = topicTitle
        ),
        testTopic(
            id = "2",
            episodes[2].id,
            title = topicTitle
        )
    )

    return episodes.mapIndexed { i, item ->
        EpisodeWithTopic(
            item,
            topics[i]
        )
    }
}

private fun testEpisode(
    id: String = "0",
    title: String = ""
) = EpisodeEntity(
    id = id,
    title = title,
    date = ""
)

private fun testTopic(
    id: String = "0",
    episodeId: String,
    title: String = ""
) = TopicEntity(
    id = id,
    episodeId = episodeId,
    title = title,
    description = "",
    imageUrl = "",
    startTime = "",
    location = "",
    presenter = "",
    liveUrl = "",
    phoneNumber = "",
    email = "",
    podcastUrl = ""
)
