package com.sti.sticanteen.domain.repository

import com.sti.sticanteen.data.network.request.RegisterRequest
import com.sti.sticanteen.utils.AuthState
import com.sti.sticanteen.utils.AuthenticationState
import kotlinx.coroutines.flow.StateFlow

interface AuthenticationRepository {


    val isLoggedIn: StateFlow<AuthenticationState>

    suspend fun authenticate(
        email: String,
        password: String,
    ): AuthState<Unit>

    suspend fun register(
        request: RegisterRequest
    ): AuthState<Unit>

    suspend fun authCheck(): AuthState<Unit>

    suspend fun requestLogout()
}