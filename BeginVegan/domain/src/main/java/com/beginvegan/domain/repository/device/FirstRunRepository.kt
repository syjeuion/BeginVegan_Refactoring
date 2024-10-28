package com.beginvegan.domain.repository.device

interface FirstRunRepository {
    // 첫 실행 확인
    suspend fun isCheckFirstRun(): Boolean

    // 첫 실행 여부 업데이트
    suspend fun updateFirstRunRecord()
}