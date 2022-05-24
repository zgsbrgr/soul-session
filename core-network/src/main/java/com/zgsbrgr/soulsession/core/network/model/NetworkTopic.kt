package com.zgsbrgr.soulsession.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkTopic(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val startTime: String,
    val location: String,
    val presenter: String,
    val liveUrl: String,
    val phoneNumber: String,
    val email: String,
    val podcastUrl: String
)
