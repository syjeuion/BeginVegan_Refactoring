package com.beginvegan.data.di.bookmarks

import com.beginvegan.data.di.core.db.DataStoreModule
import com.beginvegan.data.di.core.network.NetworkModule
import com.beginvegan.data.repository.local.auth.AuthTokenDataSource
import com.beginvegan.data.repository.remote.bookmarks.BookmarkRemoteDataSource
import com.beginvegan.data.repository.remote.bookmarks.BookmarkRemoteDataSourceImpl
import com.beginvegan.data.repository.remote.bookmarks.BookmarkRepositoryImpl
import com.beginvegan.data.retrofit.bookMarks.BookmarkService
import com.beginvegan.domain.repository.bookmarks.BookmarkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, DataStoreModule::class])
@InstallIn(SingletonComponent::class)
class BookmarkModule {
    @Singleton
    @Provides
    fun provideBookmarkService(retrofit: Retrofit): BookmarkService {
        return retrofit.create(BookmarkService::class.java)
    }

    @Provides
    @Singleton
    fun provideBookmarkRemoteDataSource(
        bookmarkService: BookmarkService,
        authTokenDataSource: AuthTokenDataSource
    ): BookmarkRemoteDataSource {
        return BookmarkRemoteDataSourceImpl(bookmarkService,authTokenDataSource)
    }

    @Provides
    @Singleton
    fun provideBookmarkRepository(
        bookmarkRemoteDataSource: BookmarkRemoteDataSource
    ): BookmarkRepository {
        return BookmarkRepositoryImpl(bookmarkRemoteDataSource)
    }
}