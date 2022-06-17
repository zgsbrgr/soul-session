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

package com.zgsbrgr.soulsession.core.network.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.zgsbrgr.soulsession.core.network.BuildConfig
import com.zgsbrgr.soulsession.core.network.SoulSessionNetwork
import com.zgsbrgr.soulsession.core.network.model.NetworkEpisode
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET

/**
 * Retrofit API declaration
 */
private interface SoulSessionNetworkApi {

    @GET("episodes.json")
    suspend fun getEpisodes(): NetworkResponse<List<NetworkEpisode>>
}

private const val BaseUrl = BuildConfig.BASEURL

/**
 * Wrapper for data provided from the [BaseUrl]
 */
@Serializable
private data class NetworkResponse<T>(
    val data: T
)

/**
 * [Retrofit] backed [SoulSessionNetwork]
 */
@Singleton
class RetrofitSoulSessionNetwork @Inject constructor() : SoulSessionNetwork {

    private val networkApi = Retrofit.Builder()
        .baseUrl(BaseUrl)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                )
                .build()
        )
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(SoulSessionNetworkApi::class.java)

    override suspend fun getEpisodes(): List<NetworkEpisode> =
        networkApi.getEpisodes().data
}
