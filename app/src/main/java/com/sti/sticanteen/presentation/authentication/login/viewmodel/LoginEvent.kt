package com.sti.sticanteen.presentation.authentication.login.viewmodel

sealed class LoginEvent {
    class LoginEmailChanged(val email: String) : LoginEvent()
    class LoginPasswordChanged(val password: String) : LoginEvent()
    object LoginSubmit : LoginEvent()
}