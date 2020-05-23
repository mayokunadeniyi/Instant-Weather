package com.mayokunadeniyi.instantweather.utils



/**
 * Created by Mayokun Adeniyi on 23/05/2020.
 */

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result <out R>{

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: String): Result<Nothing>()
    object Loading: Result<Nothing>()
    data class NetworkException(val error: String): Result<Nothing>()
    sealed class HttpErrors : Result<Nothing>() {
        data class ResourceForbidden(val exception: String) : HttpErrors()
        data class ResourceNotFound(val exception: String) : HttpErrors()
        data class InternalServerError(val exception: String) : HttpErrors()
        data class BadGateWay(val exception: String) : HttpErrors()
        data class ResourceRemoved(val exception: String) : HttpErrors()
        data class RemovedResourceFound(val exception: String) : HttpErrors()
    }
}