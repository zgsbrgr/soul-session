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

package com.zgsbrgr.soulsession.core.network.fake

import com.zgsbrgr.soulsession.core.network.model.NetworkEpisode
import com.zgsbrgr.soulsession.core.network.model.NetworkTopic
import org.intellij.lang.annotations.Language

/* ktlint-disable max-line-length */

object FakeData {

    @Language("JSON")
    val fakeEpisodes = """
       [
    {
        "id": "1",
        "title": "Terri Green és Randy Hall a szombat esti Soul Sessionben!",
        "thumbnail": "",
        "date":"2022.04.30",
        "topic": {
            "id" : "1",
            "title": "A szombat esti Soul Sessionben ismét  legendákat köszönthetünk köreinkben.",
            "episode_id": "1",
            "description": "Online csatlakozik hozzánk Terri Green és Randy Hall, akik jelenlétükkel tarkítják szombat esténket. Egy meglepetés bejelentéssel is készülünk, egy világhírű zeneszerző, hangszerelő telefonon üzen nekünk.A csoda ezen a szombaton is megtörténik, biztosan mondhatjuk, hogy ismét egy izgalmas, fergeteges est áll előttünk!",
            "image": "https://jazzy.hu/wp-content/uploads/2022/04/76ACA4D6-3443-4880-B9EF-0D7A02784ED8-1024x521.jpeg",
            "start": "19:00",
            "location": "90.9 Jazzy",
            "presenter": "Bogyó Tamás & Temesi Berci",
            "live": "JazzyTV,Jazzy Facebook",
            "phone": "06 30 20 10 909",
            "email": "soulsession@jazzy.hu",
            "podcast_url": "https://hearthis.at/909-jazzy/soul-session-2022.04.30/stream.mp3?referer=&s=hvS"
        }
    },
    {
        "id": "2",
        "title": "Énekesek éjszakája a szombat esti Soul Sessionben!",
        "thumbnail": "https://jazzy.hu/wp-content/uploads/2022/04/278671792_5306610999369986_2852743997620834752_n-150x150.jpg",
        "date":"2022.04.23",
        "topic": {
            "id" : "2",
            "title": "A csoda ezen a szombaton is megtörténik, biztosan mondhatjuk, hogy ismét egy fergeteges est áll előttünk.",
            "episode_id": "2",
            "description": "Újra élőben érkezünk! Stúdió vendégünk az idei Sztárban Sztár győztese, Vavra Bence énekes lesz, akivel együtt pörgetjük a soul ritkaságokat. Rengeteg különlegességgel és persze meglepetéssel is készülünk az estére. Várunk mindenkit szombaton, a Soul Sessionben!",
            "image": "https://jazzy.hu/wp-content/uploads/2022/04/278671792_5306610999369986_2852743997620834752_n.jpg",
            "start": "19:00",
            "location": "90.9 Jazzy",
            "presenter": "Bogyó Tamás & Temesi Berci",
            "live": "JazzyTV,Jazzy Facebook",
            "phone": "06 30 20 10 909",
            "email": "soulsession@jazzy.hu",
            "podcast_url": "https://hearthis.at/909-jazzy/soul-session-2022.04.23/stream.mp3?referer=&s=FZY"
        }
    }
    ] 
    """.trimIndent()

    @Language("JSON")
    val fakeTopics = """
[
  {
    "title": "A szombat esti Soul Sessionben ismét  legendákat köszönthetünk köreinkben.",
    "description": "Online csatlakozik hozzánk Terri Green és Randy Hall, akik jelenlétükkel tarkítják szombat esténket. Egy meglepetés bejelentéssel is készülünk, egy világhírű zeneszerző, hangszerelő telefonon üzen nekünk.A csoda ezen a szombaton is megtörténik, biztosan mondhatjuk, hogy ismét egy izgalmas, fergeteges est áll előttünk!",
    "image": "https://jazzy.hu/wp-content/uploads/2022/04/76ACA4D6-3443-4880-B9EF-0D7A02784ED8-1024x521.jpeg",
    "start": "19:00",
    "location": "90.9 Jazzy",
    "presenter": "Bogyó Tamás & Temesi Berci",
    "live": "JazzyTV,Jazzy Facebook",
    "phone": "06 30 20 10 909",
    "email": "soulsession@jazzy.hu",
    "podcast_url": "https://hearthis.at/909-jazzy/soul-session-2022.04.30/stream.mp3?referer=&s=hvS"
  },
  {
    "title": "A csoda ezen a szombaton is megtörténik, biztosan mondhatjuk, hogy ismét egy fergeteges est áll előttünk.",
    "description": "Újra élőben érkezünk! Stúdió vendégünk az idei Sztárban Sztár győztese, Vavra Bence énekes lesz, akivel együtt pörgetjük a soul ritkaságokat. Rengeteg különlegességgel és persze meglepetéssel is készülünk az estére. Várunk mindenkit szombaton, a Soul Sessionben!",
    "image": "https://jazzy.hu/wp-content/uploads/2022/04/278671792_5306610999369986_2852743997620834752_n.jpg",
    "start": "19:00",
    "location": "90.9 Jazzy",
    "presenter": "Bogyó Tamás & Temesi Berci",
    "live": "JazzyTV,Jazzy Facebook",
    "phone": "06 30 20 10 909",
    "email": "soulsession@jazzy.hu",
    "podcast_url": "https://hearthis.at/909-jazzy/soul-session-2022.04.23/stream.mp3?referer=&s=FZY"
  }
  
]        
    """.trimIndent()

    val fakeTopic = NetworkTopic(
        id = "1",
        title = "A szombat esti Soul Sessionben ismét  legendákat köszönthetünk köreinkben.",
        episodeId = "1",
        description = "Online csatlakozik hozzánk Terri Green és Randy Hall, akik jelenlétükkel tarkítják szombat esténket. Egy meglepetés bejelentéssel is készülünk, egy világhírű zeneszerző, hangszerelő telefonon üzen nekünk.A csoda ezen a szombaton is megtörténik, biztosan mondhatjuk, hogy ismét egy izgalmas, fergeteges est áll előttünk!",
        image = "https://jazzy.hu/wp-content/uploads/2022/04/76ACA4D6-3443-4880-B9EF-0D7A02784ED8-1024x521.jpeg",
        start = "19:00",
        location = "90.9 Jazzy",
        presenter = "Bogyó Tamás & Temesi Berci",
        live = "JazzyTV,Jazzy Facebook",
        phone = "06 30 20 10 909",
        email = "soulsession@jazzy.hu",
        podcastUrl = "https://hearthis.at/909-jazzy/soul-session-2022.04.30/stream.mp3?referer=&s=hvS"

    )

    val fakeEpisode = NetworkEpisode(
        id = "1",
        thumbnail = "",
        title = "Terri Green és Randy Hall a szombat esti Soul Sessionben!",
        date = "2022.04.30",
        topic = fakeTopic

    )
}
