package com.sti.sticanteen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sti.sticanteen.domain.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    val isLoggedInState = authenticationRepository.isLoggedIn
    val isLoggedIn get() = isLoggedInState.value


    init {
        viewModelScope.launch {
            authenticationRepository.authCheck()
        }
    }
}