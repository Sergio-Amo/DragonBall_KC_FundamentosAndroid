package com.kc.dragonball_kc_fundamentos.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kc.dragonball_kc_fundamentos.utils.BASE_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.Credentials
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<LoginState>(LoginState.Idle())
    val uiState: StateFlow<LoginState> = _uiState

    private var isValidEmail = false
    private var isValidPassword = false
    private var token: String = ""

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

    private fun areFieldsValid(): Boolean = isValidEmail && isValidPassword

    private fun setLoginButtonState() {
        _uiState.value = LoginState.LoginEnable(areFieldsValid())
    }

    fun loginClicked(user: String, password: String) {
        if (!areFieldsValid()) {
            _uiState.value = LoginState.Error("Failed to validate mail or password")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = LoginState.Loading()
            val client = OkHttpClient()
            val url = "${BASE_URL}/auth/login"
            val credentials = Credentials.basic(user, password)
            val formBody = FormBody.Builder() // response as POST
                .build()
            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", credentials)
                .post(formBody)
                .build()
            val call = client.newCall(request)
            val response = call.execute()
            _uiState.value = if (response.isSuccessful)
                response.body?.let {
                    token = it.string()
                    LoginState.SuccessLogin()
                } ?: LoginState.Error("Empty Token")
            else
                LoginState.Error(response.message)
        }
    }

}