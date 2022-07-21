package com.sti.sticanteen.presentation.authentication.register.viewmodel

import com.sti.sticanteen.utils.UiText

data class RegisterState(
    val email: String = "",
    val password: String = "",
    val phoneNumber: String = "",
    val name: String = "",
    val isLoading: Boolean = false,
    val errors: RegisterError = RegisterError()
)


data class RegisterError(
    val emailError: UiText = UiText.DynamicString(""),
    val passwordError: UiText = UiText.DynamicString(""),
    val phoneNumberError: UiText = UiText.DynamicString(""),
    val nameError: UiText = UiText.DynamicString("")
)