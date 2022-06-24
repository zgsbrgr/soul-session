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

data class Topic(
    val id: String,
    val title: String,
    val episodeId: String,
    val description: String?,
    val imageUrl: String?,
    val startTime: String?,
    val location: String?,
    val presenter: String?,
    val liveUrl: String?,
    val phoneNumber: String?,
    val email: String?,
    val podcastUrl: String?
)

val topicPreview =
    Topic(
        id = "1",
        title = "A szombat esti Soul Sessionben ismét  legendákat köszönthetünk köreinkben.",
        episodeId = "1",
        description = "Online csatlakozik hozzánk Terri Green és Randy Hall, akik jelenlétükkel tarkítják szombat esténket. Egy meglepetés bejelentéssel is készülünk, egy világhírű zeneszerző, hangszerelő telefonon üzen nekünk.A csoda ezen a szombaton is megtörténik, biztosan mondhatjuk, hogy ismét egy izgalmas, fergeteges est áll előttünk!",
        imageUrl = "https://jazzy.hu/wp-content/uploads/2022/04/76ACA4D6-3443-4880-B9EF-0D7A02784ED8-1024x521.jpeg",
        startTime = "19:00",
        location = "90.9 Jazzy",
        presenter = "Bogyó Tamás & Temesi Berci",
        liveUrl = "JazzyTV,Jazzy Facebook",
        phoneNumber = "06 30 20 10 909",
        email = "soulsession@jazzy.hu",
        podcastUrl = "https://hearthis.at/909-jazzy/soul-session-2022.04.30/stream.mp3?referer=&s=hvS"
    )
