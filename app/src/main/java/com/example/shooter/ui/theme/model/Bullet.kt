package com.example.shooter.ui.theme.model

data class Bullet(
    var x: Float,
    var y: Float,
    var speed: Float,
    var hit: Boolean = false
)