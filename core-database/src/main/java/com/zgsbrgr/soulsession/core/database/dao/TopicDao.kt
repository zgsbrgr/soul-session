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
import com.zgsbrgr.soulsession.core.database.model.TopicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicDao {

    @Query(value = "SELECT * FROM topics")
    fun getTopics(): Flow<List<TopicEntity>>

    @Query(value = "SELECT * FROM topics WHERE id = :topicId")
    fun getTopic(topicId: String): Flow<TopicEntity>

    @Query(value = "SELECT * FROM topics WHERE episodeId = :episodeId")
    fun getTopicForEpisode(episodeId: String): Flow<TopicEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreTopics(topicEntities: List<TopicEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreTopic(topicEntity: TopicEntity): Long

    @Update
    suspend fun updateTopics(topicEntities: List<TopicEntity>)

    @Transaction
    suspend fun upsertTopics(topicEntities: List<TopicEntity>) = upsert(
        items = topicEntities,
        insertMany = ::insertOrIgnoreTopics,
        updateMany = ::updateTopics
    )

    @Query(
        value = """
            DELETE FROM topics
            WHERE id in (:ids)
        """
    )
    suspend fun deleteTopics(ids: List<String>)
}
