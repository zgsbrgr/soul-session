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

import com.zgsbrgr.soulsession.core.database.dao.TopicDao
import com.zgsbrgr.soulsession.core.database.model.asExternalModel
import com.zgsbrgr.soulsession.core.model.data.Topic
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TopicRepositoryImpl @Inject constructor(
    private val topicDao: TopicDao
) : TopicRepository {

    override fun getTopicsStream(): Flow<List<Topic>> = topicDao.getTopics().map { topicEntities ->
        topicEntities.map {
            it.asExternalModel()
        }
    }

    override fun getTopicStream(topicId: String): Flow<Topic> =
        topicDao.getTopic(topicId).map { it.asExternalModel() }

    override fun getTopicStreamForEpisode(episodeId: String): Flow<Topic> =
        topicDao.getTopicForEpisode(episodeId).map {
            it.asExternalModel()
        }
}
