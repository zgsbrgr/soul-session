package com.zgsbrgr.soulsession.core.network.api



import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.zgsbrgr.soulsession.core.network.BuildConfig
import com.zgsbrgr.soulsession.core.network.SoulSessionNetwork
import com.zgsbrgr.soulsession.core.network.model.NetworkEpisode
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

import retrofit2.http.GET
import javax.inject.Inject
import javax.inject.Singleton

interface SoulSessionNetworkApi {

    @GET
    suspend fun getEpisodes(): List<NetworkEpisode>

}

private const val BaseUrl = BuildConfig.BASEURL

@Serializable
private  data class NetworkResponse<T>(
    val data: T
)

@Singleton
class RetrofitSoulSessionNetwork @Inject constructor(

): SoulSessionNetwork {

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
        networkApi.getEpisodes()

}