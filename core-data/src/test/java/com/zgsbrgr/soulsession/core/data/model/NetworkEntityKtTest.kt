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

package com.zgsbrgr.soulsession.core.data.model

import com.zgsbrgr.soulsession.core.network.model.NetworkEpisode
import com.zgsbrgr.soulsession.core.network.model.NetworkTopic
import org.junit.Assert
import org.junit.Test

class NetworkEntityKtTest {

    @Test
    fun network_topic_mapped_to_topic_entity() {

        val networkModel = NetworkTopic(
            id = "0",
            title = "topic title",
            episodeId = "0",
            description = "topic description",
            subtitle = "topic subtitle",
            image = "topic image url",
            start = "topic start time",
            location = "topic location",
            presenter = "topic presenter",
            live = "topic live url",
            phone = "topic phone number",
            email = "topic email",
            podcastUrl = "topic podcast url"
        )

        val entity = networkModel.asEntity()

        Assert.assertEquals("0", entity.id)
        Assert.assertEquals("topic title", entity.title)
        Assert.assertEquals("0", entity.episodeId)
        Assert.assertEquals("topic description", entity.description)
        Assert.assertEquals("topic subtitle", entity.subtitle)
        Assert.assertEquals("topic image url", entity.imageUrl)
        Assert.assertEquals("topic start time", entity.startTime)
        Assert.assertEquals("topic location", entity.location)
        Assert.assertEquals("topic presenter", entity.presenter)
        Assert.assertEquals("topic live url", entity.liveUrl)
        Assert.assertEquals("topic phone number", entity.phoneNumber)
        Assert.assertEquals("topic email", entity.email)
        Assert.assertEquals("topic podcast url", entity.podcastUrl)
    }

    @Test
    fun network_episode_mapped_to_episode_entity() {

        val networkTopicModel = NetworkTopic(
            id = "0",
            title = "topic title",
            episodeId = "0",
            description = "topic description",
            subtitle = "topic subtitle",
            image = "topic image url",
            start = "topic start time",
            location = "topic location",
            presenter = "topic presenter",
            live = "topic live url",
            phone = "topic phone number",
            email = "topic email",
            podcastUrl = "topic podcast url"
        )

        val networkModel = NetworkEpisode(
            id = "0",
            thumbnail = "thumbnail url",
            title = "topic title",
            date = "topic date",
            topic = networkTopicModel
        )

        val entity = networkModel.asEntity()

        Assert.assertEquals("0", entity.id)
        Assert.assertEquals("topic title", entity.title)
        Assert.assertEquals("topic date", entity.date)
    }
}
