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
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodeDao {

    @Transaction
    @Query(value = "SELECT * FROM episodes ORDER BY date DESC")
    fun getEpisodesStream(): Flow<List<EpisodeWithTopic>>

    @Transaction
    @Query(value = "SELECT * FROM episodes WHERE id = :episodeId")
    fun getEpisodeStream(episodeId: String): Flow<EpisodeWithTopic>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreEpisodes(episodeEntities: List<EpisodeEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreEpisode(episodeEntity: EpisodeEntity): Long

    @Update
    suspend fun updateEpisodes(episodeEntities: List<EpisodeEntity>)

    @Update
    suspend fun updateEpisode(episodeEntity: EpisodeEntity)

    @Transaction
    suspend fun upsertEpisodes(entities: List<EpisodeEntity>) = upsert(
        items = entities,
        insertMany = ::insertOrIgnoreEpisodes,
        updateMany = ::updateEpisodes
    )

    @Query(
        value = """
            DELETE FROM episodes
            WHERE id in (:ids)
        """
    )
    suspend fun deleteEpisodes(ids: List<String>)
}
