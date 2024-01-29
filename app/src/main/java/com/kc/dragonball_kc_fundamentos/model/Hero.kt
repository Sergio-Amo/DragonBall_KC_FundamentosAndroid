package com.kc.dragonball_kc_fundamentos.model

data class Hero (
    val name: String,
    val id:String,
    val photo: String,
    val health: Int = 100,
    val maxHealth: Int = 100,
)