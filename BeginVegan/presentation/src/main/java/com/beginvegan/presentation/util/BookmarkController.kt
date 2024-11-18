package com.beginvegan.presentation.util

import com.beginvegan.presentation.config.enumclass.Bookmarks

interface BookmarkController {
    suspend fun postBookmark(contentId:Int, contentType:Bookmarks) : Boolean
    suspend fun deleteBookmark(contentId:Int, contentType:Bookmarks): Boolean
}