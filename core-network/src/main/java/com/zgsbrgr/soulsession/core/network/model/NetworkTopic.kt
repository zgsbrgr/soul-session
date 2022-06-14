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

package com.zgsbrgr.soulsession.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkTopic(
    val id: String,
    val title: String,
    @SerialName("episode_id")
    val episodeId: String,
    val description: String,
    val image: String? = null,
    val start: String? = null,
    val location: String? = null,
    val presenter: String? = null,
    val live: String? = null,
    val phone: String? = null,
    val email: String? = null,
    @SerialName("podcast_url")
    val podcastUrl: String? = null
)
