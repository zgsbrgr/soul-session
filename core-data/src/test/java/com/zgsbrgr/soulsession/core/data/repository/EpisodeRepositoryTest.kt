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

package com.zgsbrgr.soulsession.core.data.repository

import com.zgsbrgr.soulsession.core.data.td.TestEpisodeDao
import com.zgsbrgr.soulsession.core.database.dao.EpisodeDao
import com.zgsbrgr.soulsession.core.database.model.asExternalModel
import com.zgsbrgr.soulsession.core.network.SoulSessionNetwork
import com.zgsbrgr.soulsession.core.network.api.RetrofitSoulSessionNetwork
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class EpisodeRepositoryTest {

    private lateinit var subject: EpisodeRepositoryImpl

    private lateinit var episodeDao: EpisodeDao

    private lateinit var network: SoulSessionNetwork

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {

        episodeDao = TestEpisodeDao()
        network = RetrofitSoulSessionNetwork()

        subject = EpisodeRepositoryImpl(
            ioDispatcher = testDispatcher,
            episodeDao = episodeDao,
            network = network
        )
    }

    @Test
    fun episodeRepositoryTest_dataFetchedAndSavedToDb_success() {
        runTest(testDispatcher) {
            val fromNetwork = subject.getEpisodesStream().first()
            val fromDao = episodeDao.getEpisodes().first().map {
                it.asExternalModel()
            }
            Assert.assertEquals(
                fromNetwork,
                fromDao
            )
        }
    }
}
