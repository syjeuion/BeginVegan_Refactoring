package com.beginvegan.data.repository.remote.bookmarks

import com.beginvegan.data.model.bookmarks.BookmarkRequest
import com.beginvegan.data.model.core.BaseResponse
import com.skydoves.sandwich.ApiResponse

interface BookmarkRemoteDataSource {
    suspend fun postBookmark(bookmarkRequest: BookmarkRequest): ApiResponse<BaseResponse>
    suspend fun deleteBookmark(bookmarkRequest: BookmarkRequest): ApiResponse<BaseResponse>
}