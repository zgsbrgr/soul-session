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

import com.zgsbrgr.soulsession.core.common.network.Dispatcher
import com.zgsbrgr.soulsession.core.common.network.SDispatchers.IO
import com.zgsbrgr.soulsession.core.data.model.asEntity
import com.zgsbrgr.soulsession.core.database.dao.EpisodeDao
import com.zgsbrgr.soulsession.core.database.dao.TopicDao
import com.zgsbrgr.soulsession.core.database.model.EpisodeWithTopic
import com.zgsbrgr.soulsession.core.database.model.asExternalModel
import com.zgsbrgr.soulsession.core.model.data.Episode
import com.zgsbrgr.soulsession.core.network.SoulSessionNetwork
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class EpisodeRepositoryImpl @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val episodeDao: EpisodeDao,
    private val topicDao: TopicDao,
    private val network: SoulSessionNetwork
) : EpisodeRepository {

    override suspend fun getEpisodesStream(): Flow<List<Episode>> {
        return withContext(ioDispatcher) {
            val networkEpisodes = network.getEpisodes()
            episodeDao.upsertEpisodes(
                networkEpisodes.map {
                    it.asEntity()
                }
            )
            topicDao.upsertTopics(
                networkEpisodes.map {
                    it.topic.asEntity()
                }
            )
            episodeDao.getEpisodesStream().map {
                it.map(EpisodeWithTopic::asExternalModel)
            }
        }
    }

    override fun getEpisodeStream(id: String): Flow<Episode> =
        episodeDao.getEpisodeStream(id).map {
            it.asExternalModel()
        }

    override suspend fun episodeFavorite(episodeId: String, favourite: Boolean) =
        withContext(ioDispatcher) {
            episodeDao.favoriteEpisode(episodeId, favourite)
        }
}
