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

import com.zgsbrgr.soulsession.core.common.network.Dispatcher
import com.zgsbrgr.soulsession.core.common.network.SDispatchers.IO
import com.zgsbrgr.soulsession.core.network.SoulSessionNetwork
import com.zgsbrgr.soulsession.core.network.model.NetworkEpisode
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class FakeNetwork @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val networkAsJson: Json
) : SoulSessionNetwork {

    override suspend fun getEpisodes(): List<NetworkEpisode> =
        withContext(ioDispatcher) {
            networkAsJson.decodeFromString(FakeData.fakeEpisodes)
        }
}
