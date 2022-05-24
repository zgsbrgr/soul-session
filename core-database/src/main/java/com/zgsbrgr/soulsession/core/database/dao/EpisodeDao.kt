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

package com.zgsbrgr.soulsession.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.zgsbrgr.soulsession.core.database.model.EpisodeEntity
import com.zgsbrgr.soulsession.core.database.model.EpisodeWithTopic
import com.zgsbrgr.soulsession.core.database.model.TopicEntity
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Dao
interface EpisodeDao {

    @Transaction
    @Query(value = "SELECT * FROM episodes")
    fun getEpisodes(): Flow<List<EpisodeWithTopic>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreEpisodes(episodeEntities: List<EpisodeEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreEpisode(episodeEntity: EpisodeEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreTopic(topicEntity: TopicEntity): Long

    @Update
    suspend fun updateEpisode(episodeEntity: EpisodeEntity)

    @Update
    suspend fun updateTopic(topicEntity: TopicEntity)

    suspend fun insertEpisodes(episodeWithTopics: List<EpisodeWithTopic>) {

        coroutineScope {
            episodeWithTopics.forEach { episodeWithTopic ->
                launch {
                    insertOrIgnoreEpisode(episodeWithTopic.entity)
                }
                launch {
                    insertOrIgnoreTopic(episodeWithTopic.topic)
                }
            }
        }
    }

    @Transaction
    suspend fun upsertEpisodeWithTopics(entities: List<EpisodeWithTopic>) {
        coroutineScope {
            entities.forEach { entity ->
                launch {
                    launch {
                        upsert(
                            item = entity.entity,
                            insert = ::insertOrIgnoreEpisode,
                            update = ::updateEpisode
                        )
                    }
                    launch {
                        upsert(
                            item = entity.topic,
                            insert = ::insertOrIgnoreTopic,
                            update = ::updateTopic
                        )
                    }
                }
            }
        }
    }

    @Query(
        value = """
            DELETE FROM episodes
            WHERE id in (:ids)
        """
    )
    suspend fun deleteEpisodes(ids: List<String>)
}
