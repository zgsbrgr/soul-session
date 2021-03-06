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

package com.zgsbrgr.soulsession.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zgsbrgr.soulsession.core.database.dao.EpisodeDao
import com.zgsbrgr.soulsession.core.database.dao.TopicDao
import com.zgsbrgr.soulsession.core.database.model.EpisodeEntity
import com.zgsbrgr.soulsession.core.database.model.TopicEntity

@Database(
    entities = [
        EpisodeEntity::class,
        TopicEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SoulSessionDatabase : RoomDatabase() {

    abstract fun episodeDao(): EpisodeDao
    abstract fun topicDao(): TopicDao
}
