package com.beginvegan.data.model.bookmarks

import com.squareup.moshi.Json

data class BookmarkRequest(
    @Json(name = "contentId")
    val contentId: Int,
    @Json(name = "contentType")
    val contentType: String
)
