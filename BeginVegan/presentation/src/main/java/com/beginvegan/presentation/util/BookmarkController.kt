package com.beginvegan.presentation.util

interface BookmarkController {
    suspend fun postBookmark(contentId:Int, contentType:String) : Boolean
    suspend fun deleteBookmark(contentId:Int, contentType:String): Boolean
}