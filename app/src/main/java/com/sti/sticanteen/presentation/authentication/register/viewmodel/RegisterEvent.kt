package com.sti.sticanteen.presentation.authentication.register.viewmodel

sealed class RegisterEvent {
    class RegisterNameChanged(val name: String) : RegisterEvent()
    class RegisterEmailChanged(val email: String) : RegisterEvent()
    class RegisterPhoneNumberChanged(val phoneNumber: String) : RegisterEvent()
    class RegisterPasswordChanged(val password: String) : RegisterEvent()
    object RegisterSubmit: RegisterEvent()
}