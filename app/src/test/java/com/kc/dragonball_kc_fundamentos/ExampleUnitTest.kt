package com.kc.dragonball_kc_fundamentos

import com.kc.dragonball_kc_fundamentos.model.Hero
import com.kc.dragonball_kc_fundamentos.ui.home.SharedViewModel
import com.kc.dragonball_kc_fundamentos.ui.login.LoginViewModel
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.min

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private val loginViewModel = LoginViewModel()
    private val hero: Hero = Hero("name", "id", "photo", 80)

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
    fun `test hero hit`() {
        // Check the damage stays between 10 and 60
        repeat(50) {
            hero.health = 100
            hero.getHit()
            assert(hero.health in 40..90)
        }
        // Check health doesn't go below 0
        repeat(50) {
            hero.health = 10
            hero.getHit()
            assertEquals(0, hero.health)
        }
    }

    @Test
    fun `test hero heal`() {
        // Check heal is 20 and don't go beyond maxhealth
        hero.health = 3
        while (hero.health < hero.maxHealth) {
            var healthBefore = hero.health
            hero.getHeal()
            assertEquals(hero.health, min(hero.maxHealth, healthBefore + 20))
            assert(hero.health <= hero.maxHealth)
        }
        assert(hero.health <= 100)
    }
}