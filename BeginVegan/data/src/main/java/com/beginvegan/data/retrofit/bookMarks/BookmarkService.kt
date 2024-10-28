package com.beginvegan.data.retrofit.bookMarks

import com.beginvegan.data.model.bookmarks.BookmarkRequest
import com.beginvegan.data.model.core.BaseResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST

interface BookmarkService {
    @POST("/api/v1/bookmarks")
    suspend fun postBookmark(
        @Header("Authorization") token: String,
        @Body bookmarkRequest: BookmarkRequest
    ): ApiResponse<BaseResponse>

    @HTTP(method = "DELETE", path = "/api/v1/bookmarks", hasBody = true)
    suspend fun deleteBookmark(
        @Header("Authorization") token: String,
        @Body bookmarkRequest: BookmarkRequest
    ): ApiResponse<BaseResponse>
}