package com.beginvegan.presentation.network

sealed class NetworkResult<out T> {
    data object Loading : NetworkResult<Nothing>()
    data object Empty : NetworkResult<Nothing>()

    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val exception: Throwable) : NetworkResult<Nothing>()
}