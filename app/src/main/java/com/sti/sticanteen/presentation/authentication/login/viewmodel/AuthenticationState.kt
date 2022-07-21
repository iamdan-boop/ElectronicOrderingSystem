package com.sti.sticanteen.presentation.authentication.login.viewmodel


data class AuthenticationState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
)