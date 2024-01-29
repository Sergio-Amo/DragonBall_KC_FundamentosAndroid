package com.kc.dragonball_kc_fundamentos.ui.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel: ViewModel() {

    private val _uiState = MutableStateFlow<LoginState>(LoginState.Idle())
    val uiState: StateFlow<LoginState> = _uiState

    sealed class LoginState {
        class Idle : LoginState()
        class Error(val errorMessage: String) : LoginState()
        class Loading : LoginState()
        class SuccessLogin : LoginState()
    }
}