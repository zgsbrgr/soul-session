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

package com.zgsbrgr.soulsession.core.model.data

/* ktlint-disable max-line-length */

data class Episode(
    val id: String,
    val thumbnail: String,
    val title: String,
    val date: String,
    val favorite: Boolean,
    val topic: Topic
)

val previewEpisode =
    Episode(
        id = "1",
        thumbnail = "https://jazzy.hu/wp-content/uploads/2022/04/278671792_5306610999369986_2852743997620834752_n.jpg",
        title = "Terri Green és Randy Hall a szombat esti Soul Sessionben!",
        date = "2022.04.05",
        favorite = false,
        topic = topicPreview
    )
