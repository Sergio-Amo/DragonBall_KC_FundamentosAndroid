package com.kc.dragonball_kc_fundamentos.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.kc.dragonball_kc_fundamentos.databinding.ActivityLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setInitialState()
        setObservers()
        setListeners()
    }

    /* Some previous configurations to the layouts */
    private fun setInitialState() {
        // Set background image to crop top
        binding.landscapeImage?.let { view ->
            val matrix = view.imageMatrix
            val viewWidth = view.drawable.intrinsicWidth.toFloat()
            val screenWidth = resources.displayMetrics.widthPixels.toFloat()
            val scale = screenWidth / viewWidth
            matrix.postScale(scale, scale)
            view.imageMatrix = matrix
        }
        // Set button state
        viewModel.validateEmail(binding.editTextTextEmailAddress.text.toString())
        viewModel.validatePassword(binding.editTextTextPassword.text.toString())
    }

    private fun setListeners() {
        binding.loginButton?.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString()
            val password = binding.editTextTextPassword.text.toString()
            viewModel.loginClicked(email, password)
        }
        //Text change listeners
        binding.editTextTextEmailAddress.doAfterTextChanged { viewModel.validateEmail(it.toString()) }
        binding.editTextTextPassword.doAfterTextChanged { viewModel.validatePassword(it.toString()) }
    }

    private fun setObservers() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.uiState.collect { state ->
                when (state) {
                    is LoginViewModel.LoginState.Idle -> idle()
                    is LoginViewModel.LoginState.LoginEnable -> enableLogin(state.enabled)
                    is LoginViewModel.LoginState.Error -> showError(state.errorMessage)
                    is LoginViewModel.LoginState.Loading -> showLoading(true)
                    is LoginViewModel.LoginState.SuccessLogin -> successLogin()
                }
            }
        }
    }

    private fun idle() {
        showLoading(false)
    }

    private fun enableLogin(enable: Boolean) {
        binding.loginButton.isEnabled = enable
    }

    private fun showError(error: String) {
        showLoading(false)
        Toast.makeText(this@LoginActivity,"Error: $error", Toast.LENGTH_LONG).show()
    }

    private fun showLoading(show: Boolean) {
        binding.loadingSpinner.root.isVisible = show
    }

    private fun successLogin() {
        showLoading(false)
        Toast.makeText(this@LoginActivity,"Login Success", Toast.LENGTH_SHORT).show()
    }
}