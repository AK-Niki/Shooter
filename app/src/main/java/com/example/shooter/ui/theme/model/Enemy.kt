package com.example.shooter.ui.theme.model

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D

data class Enemy(
    val x: Float,
    val y: Animatable<Float, AnimationVector1D>,
    val drawable: Int,
    val speed: Float = 5f,
    var health: Int
)