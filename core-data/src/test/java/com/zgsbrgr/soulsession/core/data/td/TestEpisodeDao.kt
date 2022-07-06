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

package com.zgsbrgr.soulsession.core.data.td

import com.zgsbrgr.soulsession.core.database.dao.EpisodeDao
import com.zgsbrgr.soulsession.core.database.model.EpisodeEntity
import com.zgsbrgr.soulsession.core.database.model.EpisodeWithTopic
import com.zgsbrgr.soulsession.core.database.model.TopicEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class TestEpisodeDao : EpisodeDao {

    private var entitiesStateFlow = MutableStateFlow(
        listOf(
            EpisodeEntity(
                id = "0",
                thumbnail = "thumbnail",
                title = "title",
                date = "date",
                favorite = false
            )
        )

    )

    private var entityStateFlow = MutableStateFlow(
        EpisodeEntity(
            id = "0",
            thumbnail = "thumbnail",
            title = "title",
            date = "date",
            favorite = false
        )
    )

    override fun getEpisodesStream(): Flow<List<EpisodeWithTopic>> =
        entitiesStateFlow.map {
            it.map(EpisodeEntity::asEpisodeWithTopic)
        }

    override fun getEpisodeStream(episodeId: String): Flow<EpisodeWithTopic> =
        entityStateFlow.map {
            it.asEpisodeWithTopic()
        }

    override suspend fun insertOrIgnoreEpisodes(episodeEntities: List<EpisodeEntity>): List<Long> {
        entitiesStateFlow.value = episodeEntities
        return episodeEntities.map { it.id.toLong() }
    }

    override suspend fun insertOrIgnoreEpisode(episodeEntity: EpisodeEntity): Long {
        entityStateFlow.value = episodeEntity
        return episodeEntity.id.toLong()
    }

    override suspend fun favoriteEpisode(episodeId: String, favorite: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun updateEpisodes(episodeEntities: List<EpisodeEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun upsertEpisodes(entities: List<EpisodeEntity>) {
        super.upsertEpisodes(entities)
    }

    override suspend fun updateEpisode(episodeEntity: EpisodeEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteEpisodes(ids: List<String>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateEpisode(id: String, thumbnail: String, title: String, date: String) {
        TODO("Not yet implemented")
    }
}

private fun EpisodeEntity.asEpisodeWithTopic() = EpisodeWithTopic(
    episode = this,
    topic = TopicEntity(
        id = "0",
        episodeId = this.id,
        title = "title",
        description = "description",
        subtitle = "subtitle",
        imageUrl = "image url",
        startTime = "start time",
        location = "location",
        presenter = "presenter",
        liveUrl = "live url",
        phoneNumber = "phone number",
        email = "email",
        podcastUrl = "podcast url"
    )

)
