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

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class FakeNetworkTest {

    private lateinit var subject: FakeNetwork

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {

        subject = FakeNetwork(
            ioDispatcher = testDispatcher,
            networkAsJson = Json { ignoreUnknownKeys = true }
        )
    }

    @Test
    fun testDeserializationOfEpisodes() = runTest(testDispatcher) {
        Assert.assertEquals(
            FakeData.fakeEpisode,
            subject.getEpisodes().first()
        )
    }
}
