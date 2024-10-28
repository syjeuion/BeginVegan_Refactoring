package com.beginvegan.domain.repository.bookmarks

interface BookmarkRepository {
    suspend fun postBookmark(contentId:Int, contentType:String): Result<Boolean>
    suspend fun deleteBookmark(contentId:Int, contentType:String): Result<Boolean>
}