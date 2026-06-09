package com.example.wordenglish.domain.repository

import com.example.wordenglish.domain.model.Word

interface WordQueueRepository {
    suspend fun getCurrentWord(): Word?
    suspend fun getCount(): Int
    suspend fun initializeIfEmpty()
    suspend fun advanceQueue()
    suspend fun preCacheDetails(queueId: Int, wordText: String)
}
