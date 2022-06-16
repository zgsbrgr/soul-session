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

import com.zgsbrgr.soulsession.core.database.model.EpisodeEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

/* ktlint-disable max-line-length */

class EpisodeDaoTest : TestDatabase() {

    @Test
    fun episodeDao_fetches_episodes() = runTest {

        val episodeEntities = testEpisodes()

        val topicEntityShells = episodeEntities.map(EpisodeEntity::topicEntityShell)

        episodeDao.upsertEpisodes(episodeEntities)

        topicDao.insertOrIgnoreTopics(topicEntityShells)

        val savedEpisodeEntities = episodeDao.getEpisodesStream().first()

        Assert.assertEquals(
            listOf("0", "1", "2"),
            savedEpisodeEntities.map {
                it.episode.id
            }
        )
    }

    @Test
    fun episodeDao_deletes_episodes() = runTest {

        val episodeEntities = testEpisodes()

        episodeDao.insertOrIgnoreEpisodes(episodeEntities)

        episodeDao.deleteEpisodes(listOf("0", "1", "2"))

        val savedEpisodeEntities = episodeDao.getEpisodesStream().first()

        Assert.assertTrue(savedEpisodeEntities.isEmpty())
    }
}
