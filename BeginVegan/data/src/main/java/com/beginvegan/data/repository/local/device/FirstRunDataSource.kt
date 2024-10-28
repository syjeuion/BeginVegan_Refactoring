package com.beginvegan.data.repository.local.device

interface FirstRunDataSource {
    suspend fun isFirstRun(): Boolean

    suspend fun setFirstRunCompleted()
}