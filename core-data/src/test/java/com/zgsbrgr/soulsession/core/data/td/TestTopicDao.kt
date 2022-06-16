package com.zgsbrgr.soulsession.core.data.td

import com.zgsbrgr.soulsession.core.database.dao.TopicDao
import com.zgsbrgr.soulsession.core.database.model.TopicEntity
import kotlinx.coroutines.flow.Flow

class TestTopicDao: TopicDao {

    override suspend fun insertOrIgnoreTopics(topicEntities: List<TopicEntity>): List<Long> {
        TODO("Not yet implemented")
    }

    override fun getTopics(): Flow<List<TopicEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertOrIgnoreTopic(topicEntity: TopicEntity): Long {
        TODO("Not yet implemented")
    }

    override suspend fun updateTopics(topicEntities: List<TopicEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun upsertTopics(topicEntities: List<TopicEntity>) {
        super.upsertTopics(topicEntities)
    }

    override suspend fun deleteTopics(ids: List<String>) {
        TODO("Not yet implemented")
    }
}