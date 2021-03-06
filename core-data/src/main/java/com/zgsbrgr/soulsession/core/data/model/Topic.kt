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

import com.zgsbrgr.soulsession.core.database.model.TopicEntity
import com.zgsbrgr.soulsession.core.network.model.NetworkTopic

fun NetworkTopic.asEntity() = TopicEntity(
    id = id,
    title = title,
    episodeId = episodeId,
    description = description,
    subtitle = subtitle,
    imageUrl = image,
    startTime = start,
    location = location,
    presenter = presenter,
    liveUrl = live,
    phoneNumber = phone,
    email = email,
    podcastUrl = podcastUrl
)
