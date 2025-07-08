package com.example.shooter.ui.theme.viewmodel

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.shooter.R
import com.example.shooter.ui.theme.model.Bullet
import com.example.shooter.ui.theme.model.Enemy
import com.example.shooter.ui.theme.utils.SoundManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import kotlin.random.Random

class GameViewModel(private val soundManager: SoundManager) : ViewModel() {
    val playerX = Animatable(0f)
    val bullets = mutableStateListOf<Bullet>()
    val enemies = mutableStateListOf<Enemy>()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    private val _hp = MutableStateFlow(3)
    val hp: StateFlow<Int> = _hp

    private val _isGameOver = MutableStateFlow(false)
    val isGameOver: StateFlow<Boolean> = _isGameOver

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun init(screenWidth: Int) {
        scope.launch {
            playerX.snapTo((screenWidth - 64f) / 2f)
        }
    }

    fun movePlayer(dragAmount: Float, screenWidth: Int) {
        scope.launch {
            val newX = (playerX.value + dragAmount).coerceIn(0f, screenWidth - 64f)
            playerX.snapTo(newX)
        }
    }

    fun startSpawningEnemies() {
        scope.launch {
            while (!_isGameOver.value) {
                delay(1200L)
                val type = Random.nextInt(3)
                val drawable = when (type) {
                    0 -> R.drawable.enemy
                    1 -> R.drawable.enemy2
                    else -> R.drawable.boss
                }
                val health = when (drawable) {
                    R.drawable.enemy -> 1
                    R.drawable.enemy2 -> 2
                    else -> 3
                }
                enemies.add(
                    Enemy(
                        x = Random.nextFloat() * 800f,
                        y = Animatable(0f),
                        drawable = drawable,
                        speed = if (drawable == R.drawable.boss) 2f else 5f,
                        health = health
                    )
                )
            }
        }
    }

    fun startShooting(screenHeight: Int) {
        scope.launch {
            while (!_isGameOver.value) {
                delay(300L)
                bullets.add(
                    Bullet(
                        x = playerX.value + 32f,
                        y = screenHeight - 84f,
                        speed = -20f
                    )
                )
                soundManager.playFire()
            }
        }
    }

    fun handleCollisions(screenHeight: Int, onGameOver: () -> Unit) {
        scope.launch {
            while (isActive && !_isGameOver.value) {
                delay(16L) // ~60 FPS

                bullets.forEach { it.y += it.speed }
                bullets.removeAll { it.y < 0 }

                enemies.forEach {
                    scope.launch { it.y.snapTo(it.y.value + it.speed) }
                }

                val hitEnemies = mutableSetOf<Enemy>()
                val hitBullets = mutableSetOf<Bullet>()

                for (enemy in enemies) {
                    val enemyRect = Rect(Offset(enemy.x, enemy.y.value), Size(64f, 64f))
                    for (bullet in bullets) {
                        val bulletRect = Rect(Offset(bullet.x, bullet.y), Size(8f, 16f))
                        if (bulletRect.overlaps(enemyRect.inflate(24f))) {
                            bullet.hit = true
                            enemy.health--
                            soundManager.playHit()
                            if (enemy.health <= 0) {
                                hitEnemies.add(enemy)
                                _score.value += 1
                                if (_score.value % 100 == 0) {
                                    _hp.value += 1
                                    soundManager.playBonus()
                                }
                            }
                            hitBullets.add(bullet)
                            break
                        }
                    }
                }

                enemies.removeAll(hitEnemies)
                bullets.removeAll(hitBullets)

                enemies.firstOrNull { it.y.value > screenHeight }?.let {
                    _hp.value--
                    enemies.clear()
                    if (_hp.value <= 0) {
                        _isGameOver.value = true
                        onGameOver()
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}