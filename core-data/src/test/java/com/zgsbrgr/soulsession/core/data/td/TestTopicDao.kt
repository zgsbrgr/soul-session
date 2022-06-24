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

import com.zgsbrgr.soulsession.core.database.dao.TopicDao
import com.zgsbrgr.soulsession.core.database.model.TopicEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/* ktlint-disable max-line-length */

class TestTopicDao : TopicDao {

    private var entitiesStateFlow = MutableStateFlow(
        listOf(
            TopicEntity(
                id = "42",
                episodeId = "0",
                title = "Ma este 7-kor ismét Soul Session Bogyó Tamással és Temesi Bercivel a 90.9 Jazzy-n!",
                description = "A mai adásban bemutatjukCooper & Ross1982-es ismeretlen duett LP-jét! Továbbá két fontos bejelentésünk is lesz a következő műsorokkal kapcsolatban, tartsatok velünk![timed-content-server hide=”2021-01-09 21:00 Europe/Budapest”]Hallgasd online!Your browser does not support the audio element.",
                imageUrl = "https://jazzy.hu/wp-content/uploads/2021/01/361BE925-D891-4ECE-94E9-97182AA380C4-284x300.jpeg",
                startTime = "19:00",
                phoneNumber = "06 30 20 10 909",
                email = "soulsession@jazzy.hu",
                liveUrl = "JazzyTV",
                location = "90.9 Jazzy,JazzyTV, Jazzy Facebook",
                presenter = "Bogyó Tamás & Temesi Berci",
                podcastUrl = "https://hearthis.at/909-jazzy/soul-session-2021.05.29/stream.mp3?referer=&s=DF1"
            )
        )
    )

    private var entityStateFlow = MutableStateFlow(
        TopicEntity(
            id = "42",
            episodeId = "0",
            title = "Ma este 7-kor ismét Soul Session Bogyó Tamással és Temesi Bercivel a 90.9 Jazzy-n!",
            description = "A mai adásban bemutatjukCooper & Ross1982-es ismeretlen duett LP-jét! Továbbá két fontos bejelentésünk is lesz a következő műsorokkal kapcsolatban, tartsatok velünk![timed-content-server hide=”2021-01-09 21:00 Europe/Budapest”]Hallgasd online!Your browser does not support the audio element.",
            imageUrl = "https://jazzy.hu/wp-content/uploads/2021/01/361BE925-D891-4ECE-94E9-97182AA380C4-284x300.jpeg",
            startTime = "19:00",
            phoneNumber = "06 30 20 10 909",
            email = "soulsession@jazzy.hu",
            liveUrl = "JazzyTV",
            location = "90.9 Jazzy,JazzyTV, Jazzy Facebook",
            presenter = "Bogyó Tamás & Temesi Berci",
            podcastUrl = "https://hearthis.at/909-jazzy/soul-session-2021.05.29/stream.mp3?referer=&s=DF1"
        )
    )

    override suspend fun insertOrIgnoreTopics(topicEntities: List<TopicEntity>): List<Long> {
        entitiesStateFlow.value = topicEntities
        return topicEntities.map { it.id.toLong() }
    }

    override fun getTopics(): Flow<List<TopicEntity>> =
        entitiesStateFlow

    override suspend fun insertOrIgnoreTopic(topicEntity: TopicEntity): Long {
        entityStateFlow.value = topicEntity
        return topicEntity.id.toLong()
    }

    override suspend fun updateTopics(topicEntities: List<TopicEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun upsertTopics(topicEntities: List<TopicEntity>) {
        super.upsertTopics(topicEntities)
    }

    override suspend fun deleteTopics(ids: List<String>) {
        TODO("Not yet implemented")
    }

    override fun getTopic(topicId: String): Flow<TopicEntity> {
        return entityStateFlow
    }

    override fun getTopicForEpisode(episodeId: String): Flow<TopicEntity> {
        return entityStateFlow
    }
}
