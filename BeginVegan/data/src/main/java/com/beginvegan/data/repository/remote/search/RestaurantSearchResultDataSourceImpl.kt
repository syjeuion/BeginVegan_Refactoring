package com.beginvegan.data.repository.remote.search

import com.beginvegan.data.model.map.VeganMapRestaurantResponse
import com.beginvegan.data.repository.local.auth.AuthTokenDataSource
import com.beginvegan.data.retrofit.map.VeganMapService
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class RestaurantSearchResultDataSourceImpl @Inject constructor(
    private val veganMapService: VeganMapService,
    private val authTokenDataSource: AuthTokenDataSource
) : RestaurantSearchResultDataSource {
    override suspend fun getRestaurantSearchResult(
        page: Int,
        latitude: String,
        longitude: String,
        searchWord: String,
        filter: String
    ): ApiResponse<VeganMapRestaurantResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"
        return veganMapService.getSearchRestaurant(
            authHeader,
            page,
            latitude,
            longitude,
            searchWord,
            filter
        )
            .suspendOnSuccess {
                Timber.d("getRestaurantSearchResult successful${this.data}")
                ApiResponse.Success(this.data)
            }.suspendOnError {
                Timber.e("getRestaurantSearchResult error: ${this.errorBody}")
                ApiResponse.Failure.Error(this)
            }
    }
}