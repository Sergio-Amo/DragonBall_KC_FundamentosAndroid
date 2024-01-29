package com.kc.dragonball_kc_fundamentos.ui.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<LoginState>(LoginState.Idle())
    val uiState: StateFlow<LoginState> = _uiState

    private var isValidEmail = false
    private var isValidPassword = false

    sealed class LoginState {
        class Idle : LoginState()
        class LoginEnable(val enabled: Boolean) : LoginState()
        class Error(val errorMessage: String) : LoginState()
        class Loading : LoginState()
        class SuccessLogin : LoginState()
    }

    fun validateEmail(str: String) {
        // Validates email in (valid chars) (@) (char/s) (dot) (chars)
        val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)\\..+\$".toRegex()
        isValidEmail = emailRegex.matches(str)
        setLoginButtonState()
    }
    // I can't validate the password too much are some users has very short passwords
    fun validatePassword(str: String) {
        isValidPassword = str.length > 2
        setLoginButtonState()
    }

    private fun setLoginButtonState() {
        if (isValidEmail && isValidPassword)
            _uiState.value = LoginState.LoginEnable(true)
        else
            _uiState.value = LoginState.LoginEnable(false)
    }

}