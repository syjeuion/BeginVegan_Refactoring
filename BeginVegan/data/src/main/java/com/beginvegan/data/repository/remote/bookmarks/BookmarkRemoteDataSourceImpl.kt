package com.beginvegan.data.repository.remote.bookmarks

import com.beginvegan.data.model.bookmarks.BookmarkRequest
import com.beginvegan.data.model.core.BaseResponse
import com.beginvegan.data.repository.local.auth.AuthTokenDataSource
import com.beginvegan.data.retrofit.bookMarks.BookmarkService
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class BookmarkRemoteDataSourceImpl @Inject constructor(
    private val bookmarkService: BookmarkService,
    private val authTokenDataSource: AuthTokenDataSource
):BookmarkRemoteDataSource {
    override suspend fun postBookmark(
        bookmarkRequest: BookmarkRequest
    ): ApiResponse<BaseResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"
        return bookmarkService.postBookmark(authHeader, bookmarkRequest).suspendOnSuccess {
            Timber.d("postBookmark successful")
            ApiResponse.Success(data)
        }.suspendOnError {
            Timber.e("postBookmark error: ${this.errorBody}")
            ApiResponse.Failure.Error(this)
        }
    }

    override suspend fun deleteBookmark(
        bookmarkRequest: BookmarkRequest
    ): ApiResponse<BaseResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"
        return bookmarkService.deleteBookmark(authHeader, bookmarkRequest).suspendOnSuccess {
            Timber.d("deleteBookmark successful")
            ApiResponse.Success(data)
        }.suspendOnError {
            Timber.e("deleteBookmark error: ${this.errorBody}")
            ApiResponse.Failure.Error(this)
        }
    }
}