package com.sti.canteen_ordering_app.utils

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(isLoading: Boolean = true) : Resource<T>(null)
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data = data, message = message)
}