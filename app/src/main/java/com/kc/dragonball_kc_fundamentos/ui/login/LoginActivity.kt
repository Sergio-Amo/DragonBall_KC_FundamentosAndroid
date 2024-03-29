package com.kc.dragonball_kc_fundamentos.ui.login

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.kc.dragonball_kc_fundamentos.data.repository.local.LoginRepository
import com.kc.dragonball_kc_fundamentos.databinding.ActivityLoginBinding
import com.kc.dragonball_kc_fundamentos.ui.home.HomeActivity
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
        // Set checkbox state
        val rememberCbChecked = LoginRepository.getCheckBoxValue(this)
        binding.rememberCheckBox.isChecked = rememberCbChecked
        // Set email value
        /** I decided against saving passwords or tokens cause the
         * exercise asked to use shared preferences that are not secure enough for it **/
        if (rememberCbChecked) binding.editTextTextEmailAddress.setText(
            LoginRepository.getEmailValue(this)
        )
        // Set login button state
        viewModel.validateEmail(binding.editTextTextEmailAddress.text.toString())
        viewModel.validatePassword(binding.editTextTextPassword.text.toString())
    }

    private fun setListeners() {
        // Login button click
        binding.loginButton.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString()
            val password = binding.editTextTextPassword.text.toString()
            viewModel.doLogin(email, password)
        }
        //EditText text changed listeners
        binding.editTextTextEmailAddress.doAfterTextChanged { viewModel.validateEmail(it.toString()) }
        binding.editTextTextPassword.doAfterTextChanged { viewModel.validatePassword(it.toString()) }
        // Remember user checkbox
        binding.rememberCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) LoginRepository.removeEmail(this)
            LoginRepository.saveCheckBoxState(this, isChecked)
        }
    }

    private fun setObservers() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.uiState.collect { state ->
                when (state) {
                    is LoginViewModel.LoginState.Idle -> idle()
                    is LoginViewModel.LoginState.LoginEnable -> enableLogin(state.enabled)
                    is LoginViewModel.LoginState.Error -> showError(state.errorMessage)
                    is LoginViewModel.LoginState.Loading -> showLoading(true)
                    is LoginViewModel.LoginState.SuccessLogin -> successLogin(state.token)
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
        Toast.makeText(this@LoginActivity, "Error: $error", Toast.LENGTH_LONG).show()
    }

    private fun showLoading(show: Boolean) {
        binding.loadingSpinner.root.isVisible = show
    }

    private fun successLogin(token: String) {
        // Save mail if remember checked
        if (binding.rememberCheckBox.isChecked)
            LoginRepository.saveEmailValue(this, binding.editTextTextEmailAddress.text.toString())
        showLoading(false)
        navigateToHome(token)
        //Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToHome(token: String) {
        HomeActivity.launchActivity(this@LoginActivity, token)
    }
}