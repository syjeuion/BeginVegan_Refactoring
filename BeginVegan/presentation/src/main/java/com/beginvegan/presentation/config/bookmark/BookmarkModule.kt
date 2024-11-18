package com.beginvegan.presentation.config.bookmark

import com.beginvegan.domain.useCase.bookmarks.BookmarkUseCase
import com.beginvegan.presentation.config.enumclass.Bookmarks
import com.beginvegan.presentation.util.BookmarkController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.coroutineScope

@Module
@InstallIn(ActivityComponent::class)
object BookmarkModule {

    @Provides
    fun provideBookmarkController(bookmarkUseCase: BookmarkUseCase): BookmarkController =
        object : BookmarkController {
            override suspend fun postBookmark(contentId: Int, contentType: Bookmarks): Boolean {
                var onSuccess = false
                coroutineScope {
                    bookmarkUseCase.postBookmark(contentId, contentType.type).onSuccess {
                        onSuccess = true
                    }.onFailure {
                        onSuccess = false
                    }
                }
                return onSuccess
            }

            override suspend fun deleteBookmark(contentId: Int, contentType: Bookmarks): Boolean {
                var onSuccess = false
                coroutineScope {
                    bookmarkUseCase.deleteBookmark(contentId, contentType.type).onSuccess {
                        onSuccess = true
                    }.onFailure {
                        onSuccess = false
                    }
                }
                return onSuccess
            }
        }
}