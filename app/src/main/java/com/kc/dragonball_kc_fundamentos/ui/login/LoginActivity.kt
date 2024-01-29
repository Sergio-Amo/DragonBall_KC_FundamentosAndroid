package com.kc.dragonball_kc_fundamentos.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.kc.dragonball_kc_fundamentos.databinding.ActivityLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Error

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setObservers()
        setListeners()
    }

    private fun setListeners() {
        // TODO("Not yet implemented")
        //binding.LoginButton.setOnClickListener { viewModel.LoginClicked() }
    }

    private fun setObservers() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.uiState.collect { state ->
                when (state) {
                    is LoginViewModel.LoginState.Idle -> idle()
                    is LoginViewModel.LoginState.Error -> showError(state.errorMessage)
                    is LoginViewModel.LoginState.Loading -> showLoading(true)
                    is LoginViewModel.LoginState.SuccessLogin -> successLogin()
                }
            }
        }
    }

    private fun idle() {
        // TODO("Not yet implemented")
    }

    private fun showError(error: String) {
        // TODO("Not yet implemented")
    }

    private fun showLoading(show: Boolean) {
        // TODO("Not yet implemented")
    }

    private fun successLogin() {
        // Might end unused...
        // TODO("Not yet implemented")
    }
}