package com.kc.dragonball_kc_fundamentos.data.repository.local

import android.content.Context
import com.kc.dragonball_kc_fundamentos.utils.LOGIN_CHECKBOX_CHECKED
import com.kc.dragonball_kc_fundamentos.utils.LOGIN_EMAIL_VALUE

class LoginRepository {
    companion object {
        fun getEmailValue(context: Context): String =
            context.getSharedPreferences(LOGIN_EMAIL_VALUE, Context.MODE_PRIVATE)
                .getString(LOGIN_EMAIL_VALUE, "") ?: ""


        fun getCheckBoxValue(context: Context): Boolean =
            context.getSharedPreferences(LOGIN_CHECKBOX_CHECKED, Context.MODE_PRIVATE)
                .getBoolean(LOGIN_CHECKBOX_CHECKED, false)

        fun saveCheckBoxState(context: Context, checked: Boolean) {
            context.getSharedPreferences(LOGIN_CHECKBOX_CHECKED, Context.MODE_PRIVATE).edit()
                .apply {
                    putBoolean(LOGIN_CHECKBOX_CHECKED, checked)
                    apply()
                }
        }

        fun saveEmailValue(context: Context, emailValue: String) {
            context.getSharedPreferences(LOGIN_EMAIL_VALUE, Context.MODE_PRIVATE).edit().apply {
                putString(LOGIN_EMAIL_VALUE, emailValue)
                apply()
            }
        }

        fun removeEmail(context: Context) {
            saveEmailValue(context, "")
        }
    }
}