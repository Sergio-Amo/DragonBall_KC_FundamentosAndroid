package com.kc.dragonball_kc_fundamentos.model

import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

data class Hero(
    val name: String,
    val id: String,
    val photo: String,
    var health: Int = 100,
    val maxHealth: Int = 100,
) {
    private val healValue: Int = 20
    fun getHit () {
        health = max(0, health + Random.nextInt(-60,-9))
    }

    fun getHeal() {
        health = min(maxHealth, health + healValue)
    }
}