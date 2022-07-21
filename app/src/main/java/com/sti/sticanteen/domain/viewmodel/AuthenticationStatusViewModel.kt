package com.sti.sticanteen.domain.viewmodel

import androidx.lifecycle.ViewModel
import com.sti.sticanteen.domain.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel()