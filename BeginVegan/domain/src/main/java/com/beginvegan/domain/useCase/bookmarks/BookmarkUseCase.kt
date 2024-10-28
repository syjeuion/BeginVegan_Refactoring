package com.beginvegan.domain.useCase.bookmarks

import com.beginvegan.domain.repository.bookmarks.BookmarkRepository
import javax.inject.Inject

class BookmarkUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository
) {
    suspend fun postBookmark(contentId:Int, contentType:String): Result<Boolean> =
        bookmarkRepository.postBookmark(contentId, contentType)

    suspend fun deleteBookmark(contentId:Int, contentType:String): Result<Boolean> =
        bookmarkRepository.deleteBookmark(contentId, contentType)
}