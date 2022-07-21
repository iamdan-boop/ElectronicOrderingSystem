package com.sti.sticanteen.presentation.authentication.register.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sti.sticanteen.R
import com.sti.sticanteen.data.network.request.RegisterRequest
import com.sti.sticanteen.domain.repository.AuthenticationRepository
import com.sti.sticanteen.presentation.authentication.register.viewmodel.RegisterEvent.RegisterEmailChanged
import com.sti.sticanteen.utils.AuthState
import com.sti.sticanteen.utils.UiText
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import com.wajahatkarim3.easyvalidation.core.view_ktx.validNumber
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
) : ViewModel() {

    private val _state = mutableStateOf(RegisterState())
    val state = _state


    private val _authState = Channel<AuthState<Unit>>()
    val authState = _authState.receiveAsFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEmailChanged -> _state.value = _state.value.copy(email = event.email)
            is RegisterEvent.RegisterPasswordChanged -> _state.value =
                _state.value.copy(password = event.password)
            is RegisterEvent.RegisterPhoneNumberChanged -> _state.value =
                _state.value.copy(phoneNumber = event.phoneNumber)
            is RegisterEvent.RegisterNameChanged -> _state.value =
                _state.value.copy(name = event.name)
            is RegisterEvent.RegisterSubmit -> viewModelScope.launch {
                state.value = state.value.copy(errors = RegisterError())
                register(state.value)
            }
        }
    }


    private suspend fun register(state: RegisterState) {
        if (state.name.isEmpty()) {
            _state.value = state.copy(
                errors = state.errors.copy(
                    nameError = UiText.StringResource(R.string.invalid_empty_name)
                )
            )
            return
        }

        if (!state.phoneNumber.validNumber()) {
            _state.value = state.copy(
                errors = state.errors.copy(
                    phoneNumberError = UiText.StringResource(R.string.invalid_phone_format)
                )
            )
            return
        }


        if (!state.email.validEmail() || state.email.isEmpty()) {
            _state.value = state.copy(
                errors = state.errors.copy(
                    emailError = UiText.StringResource(R.string.invalid_email_format)
                )
            )
            return
        }

        if (!state.password.validator()
                .nonEmpty()
                .minLength(6)
                .atleastOneUpperCase()
                .check()
        ) {
            _state.value = state.copy(
                errors = state.errors.copy(
                    passwordError = UiText.StringResource(R.string.password_validation_format)
                )
            )
            return
        }

        _state.value = state.copy(isLoading = true)
        val registrationResult = authenticationRepository.register(
            request = RegisterRequest(
                name = state.name,
                phoneNumber = state.phoneNumber,
                email = state.email,
                password = state.password
            )
        )
        _authState.send(registrationResult)
        _state.value = state.copy(isLoading = false)
    }
}