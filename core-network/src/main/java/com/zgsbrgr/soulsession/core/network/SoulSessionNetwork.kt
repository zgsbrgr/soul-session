package com.zgsbrgr.soulsession.core.network

import com.zgsbrgr.soulsession.core.network.model.NetworkEpisode

interface SoulSessionNetwork {

    suspend fun getEpisodes(): List<NetworkEpisode>

}