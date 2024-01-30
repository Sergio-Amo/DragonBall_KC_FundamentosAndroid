package com.kc.dragonball_kc_fundamentos.ui.login

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.kc.dragonball_kc_fundamentos.databinding.ActivityLoginBinding
import com.kc.dragonball_kc_fundamentos.utils.LOGIN_CHECKBOX_CHECKED
import com.kc.dragonball_kc_fundamentos.utils.LOGIN_EMAIL_VALUE
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
        val rememberCbChecked =
            getPreferences(Context.MODE_PRIVATE).getBoolean(LOGIN_CHECKBOX_CHECKED, false)
        binding.rememberCheckBox.isChecked = rememberCbChecked
        // Set email value
        if (rememberCbChecked) binding.editTextTextEmailAddress.setText(
            getPreferences(Context.MODE_PRIVATE).getString(LOGIN_EMAIL_VALUE, "")
        )
        // Set login button state
        viewModel.validateEmail(binding.editTextTextEmailAddress.text.toString())
        viewModel.validatePassword(binding.editTextTextPassword.text.toString())
    }

    private fun setListeners() {
        // Login button click
        binding.loginButton?.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString()
            val password = binding.editTextTextPassword.text.toString()
            viewModel.loginClicked(email, password)
        }
        //EditText text changed listeners
        binding.editTextTextEmailAddress.doAfterTextChanged { viewModel.validateEmail(it.toString()) }
        binding.editTextTextPassword.doAfterTextChanged { viewModel.validatePassword(it.toString()) }
        // Remember user checkbox
        binding.rememberCheckBox.setOnCheckedChangeListener { _, isChecked ->
            getPreferences(Context.MODE_PRIVATE).edit().apply {
                if (!isChecked) putString(LOGIN_EMAIL_VALUE, "")
                putBoolean(LOGIN_CHECKBOX_CHECKED, isChecked)
                apply()
            }
        }
    }

    fun saveEmail(emailValue: String) {
        getPreferences(Context.MODE_PRIVATE).edit().apply {
            putString(LOGIN_EMAIL_VALUE, emailValue)
            apply()
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
        Toast.makeText(this@LoginActivity, "Error: $error", Toast.LENGTH_LONG).show()
    }

    private fun showLoading(show: Boolean) {
        binding.loadingSpinner.root.isVisible = show
    }

    private fun successLogin() {
        // Save mail if remember checked
        if (binding.rememberCheckBox.isChecked) saveEmail(binding.editTextTextEmailAddress.text.toString())

        showLoading(false)
        Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()
    }
}