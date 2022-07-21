package com.sti.sticanteen.presentation.authentication.login.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sti.sticanteen.R
import com.sti.sticanteen.domain.repository.AuthenticationRepository
import com.sti.sticanteen.utils.AuthState
import com.sti.sticanteen.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {


    private val _state = mutableStateOf(AuthenticationState())
    val state = _state

    private val _stateChannel = Channel<UiText>()
    val stateChannel = _stateChannel.receiveAsFlow()

    private val resultChannel = Channel<AuthState<Unit>>()
    val authState = resultChannel.receiveAsFlow()


    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.LoginEmailChanged ->
                _state.value = state.value.copy(email = event.email)
            is LoginEvent.LoginPasswordChanged -> _state.value =
                state.value.copy(password = event.password)
            is LoginEvent.LoginSubmit -> viewModelScope.launch {
                authenticate(
                    state.value.email,
                    state.value.password
                )
            }
        }
    }


    private suspend fun authenticate(email: String, password: String) {
        _state.value = _state.value.copy(isLoading = true)
        if (email.isBlank() || password.isBlank()) {
            _stateChannel.send(UiText.StringResource(R.string.field_empty_error))
            _state.value = _state.value.copy(isLoading = false)
            return
        }
        try {
            val authenticationResult =
                authenticationRepository.authenticate(email = email, password = password)
            _state.value = _state.value.copy(isLoading = false)


            resultChannel.send(authenticationResult)
            _stateChannel.send(UiText.DynamicString("Please wait being redirected to home page,"))
        } catch (_: IOException) {
            _stateChannel.send(UiText.StringResource(R.string.poor_connection))
            _state.value = _state.value.copy(isLoading = false)
        } catch (e: HttpException) {
            _state.value = _state.value.copy(isLoading = false)
            if (e.code() == 500) {
                _stateChannel.send(UiText.StringResource(R.string.server_error))
                resultChannel.send(AuthState.UnknownError())
            }
            _stateChannel.send(UiText.StringResource(R.string.invalid_credentials))
            resultChannel.send(AuthState.Unauthorized())
        }
    }
}