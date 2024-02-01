package com.kc.dragonball_kc_fundamentos

import com.kc.dragonball_kc_fundamentos.ui.home.SharedViewModel
import com.kc.dragonball_kc_fundamentos.ui.login.LoginViewModel
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private val loginViewModel = LoginViewModel()
    private val sharedViewModel = SharedViewModel()

    @Test
    fun `test mail validation`() {
        // Invalid emails
        arrayOf("fail", "fail@", "fail@fail", "fail@fail.").map {
            assertEquals(false, loginViewModel.validateEmail(it))
        }
        // Valid emails
        arrayOf(
            "simple@example.com",
            "very.common@example.com",
            "abc@example.co.uk",
            "disposable.style.email.with+symbol@example.com",
            "other.email-with-hyphen@example.com",
            "fully-qualified-domain@example.com"
        ).map {
            assertEquals(true, loginViewModel.validateEmail(it))
        }
    }

    @Test
    fun `test2 mail validation`() {}
}