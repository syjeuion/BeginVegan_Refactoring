package com.beginvegan.data.repository.remote.bookmarks

import com.beginvegan.data.model.bookmarks.BookmarkRequest
import com.beginvegan.domain.repository.bookmarks.BookmarkRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import timber.log.Timber
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(
    private val bookmarkRemoteDataSource: BookmarkRemoteDataSource
) : BookmarkRepository{
    override suspend fun postBookmark(
        contentId: Int,
        contentType: String
    ): Result<Boolean> {
        return try {
            val request = BookmarkRequest(contentId, contentType)
            val response = bookmarkRemoteDataSource.postBookmark(request)
            when (response) {
                is ApiResponse.Success -> {
                    Result.success(true)
                }
                is ApiResponse.Failure.Error -> {
                    Timber.e("postBookmark error: ${response.errorBody}")
                    Result.failure(Exception("postBookmark failed"))
                }
                is ApiResponse.Failure.Exception -> {
                    Timber.e("postBookmark exception: ${response.message}")
                    Result.failure(response.throwable)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "postBookmark exception")
            Result.failure(e)
        }
    }

    override suspend fun deleteBookmark(
        contentId: Int,
        contentType: String
    ): Result<Boolean> {
        return try {
            val request = BookmarkRequest(contentId, contentType)
            val response = bookmarkRemoteDataSource.deleteBookmark(request)
            when (response) {
                is ApiResponse.Success -> {
                    Result.success(true)
                }
                is ApiResponse.Failure.Error -> {
                    Timber.e("deleteBookmark error: ${response.errorBody}")
                    Result.failure(Exception("deleteBookmark failed"))
                }
                is ApiResponse.Failure.Exception -> {
                    Timber.e("deleteBookmark exception: ${response.message}")
                    Result.failure(response.throwable)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "deleteBookmark exception")
            Result.failure(e)
        }
    }
}