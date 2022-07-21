package com.sti.sticanteen.utils

sealed class AuthState<T>(val data: T? = null) {
    class Authorized<T>(data: T? = null) : AuthState<T>(data)
    class Unauthorized<T> : AuthState<T>()
    class UnknownError<T> : AuthState<T>()
}