package com.example.shooter.ui.theme.screens

import android.media.SoundPool
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shooter.R
import kotlin.random.Random
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.max


@Composable
fun GameScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var screenWidth by remember { mutableStateOf(0) }
    var screenHeight by remember { mutableStateOf(0) }

    val soundPool = remember { SoundPool.Builder().setMaxStreams(5).build() }
    val fireSound = remember { soundPool.load(context, R.raw.fire, 1) }
    val hitSound = remember { soundPool.load(context, R.raw.hit, 1) }
    val explosionSound = remember { soundPool.load(context, R.raw.explosion, 1) }
    val bonusSound = remember { soundPool.load(context, R.raw.bonus, 1) }

    var score by remember { mutableStateOf(0) }
    var hp by remember { mutableStateOf(3) }
    var isGameOver by remember { mutableStateOf(false) }

    val playerX = remember { Animatable(0f) }
    val bullets = remember { mutableStateListOf<Bullet>() }
    val enemies = remember { mutableStateListOf<Enemy>() }

    val playerWidth = 64f
    val playerHeight = 64f

    var enemySpawnDelay by remember { mutableStateOf(1200L) }

    LaunchedEffect(screenWidth) {
        playerX.snapTo((screenWidth - playerWidth) / 2f)
    }

    LaunchedEffect(true) {
        while (!isGameOver) {
            delay(enemySpawnDelay)
            val enemyType = Random.nextInt(3)
            val drawable = when (enemyType) {
                0 -> R.drawable.enemy
                1 -> R.drawable.enemy2
                else -> R.drawable.boss
            }
            val health = when (enemyType) {
                0 -> 1
                1 -> 2
                else -> 3
            }
            val speed = if (enemyType == 2) 2f else 5f

            enemies.add(
                Enemy(
                    x = Random.nextFloat() * (screenWidth - 64f),
                    y = Animatable(0f),
                    drawable = drawable,
                    health = health,
                    speed = speed
                )
            )
        }
    }

    LaunchedEffect(true) {
        while (!isGameOver) {
            withFrameMillis {
                bullets.forEach { it.y += it.speed }
                bullets.removeAll { it.y < 0 }

                enemies.forEach {
                    scope.launch {
                        it.y.animateTo(it.y.value + it.speed)
                    }
                }

                val hitBullets = mutableSetOf<Bullet>()

                for (bullet in bullets) {
                    val bulletRect = Rect(
                        offset = Offset(bullet.x, bullet.y),
                        size = Size(8f, 16f)
                    )

                    val hitEnemy = enemies.firstOrNull { enemy ->
                        val enemyRect = Rect(
                            offset = Offset(enemy.x, enemy.y.value),
                            size = Size(64f, 64f)
                        )
                        bulletRect.overlaps(enemyRect.inflate(24f))
                    }

                    if (hitEnemy != null) {
                        hitEnemy.health -= 1
                        hitBullets.add(bullet)
                        soundPool.play(hitSound, 1f, 1f, 0, 0, 1f)

                        if (hitEnemy.health <= 0) {
                            enemies.remove(hitEnemy)
                            score++
                            if (score % 100 == 0) {
                                hp++
                                soundPool.play(bonusSound, 1f, 1f, 0, 0, 1f)
                            }
                            if (score % 200 == 0) {
                                enemySpawnDelay = (enemySpawnDelay * 0.9).toLong().coerceAtLeast(300L)
                            }
                        }
                    }
                }

                bullets.removeAll(hitBullets)

                enemies.firstOrNull { it.y.value > screenHeight }?.let {
                    hp--
                    enemies.clear()
                    if (hp <= 0) {
                        isGameOver = true
                        navController.navigate("gameover")
                    }
                }
            }
        }
    }

    LaunchedEffect(true) {
        while (!isGameOver) {
            delay(300L)
            bullets.add(
                Bullet(
                    x = playerX.value + (playerWidth / 2),
                    y = screenHeight - playerHeight - 20f,
                    speed = -20f
                )
            )
            soundPool.play(fireSound, 1f, 1f, 0, 0, 1f)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned {
                screenWidth = it.size.width
                screenHeight = it.size.height
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    scope.launch {
                        val newX = (playerX.value + dragAmount).coerceIn(0f, screenWidth - playerWidth)
                        playerX.snapTo(newX)
                    }
                }
            }
    ) {
        // Фон пола
        Image(
            painter = painterResource(id = R.drawable.floor),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        // Враги
        enemies.forEach { e ->
            Image(
                painter = painterResource(id = e.drawable),
                contentDescription = null,
                modifier = Modifier
                    .offset { IntOffset(e.x.toInt(), e.y.value.toInt()) }
                    .size(64.dp)
            )
        }

        // Пули
        bullets.forEach { b ->
            Box(
                Modifier
                    .offset { IntOffset(b.x.toInt(), b.y.toInt()) }
                    .size(8.dp, 16.dp)
                    .background(Color.Yellow)
            )
        }

        // Игрок
        Image(
            painter = painterResource(id = R.drawable.player),
            contentDescription = null,
            modifier = Modifier
                .offset { IntOffset(playerX.value.toInt(), screenHeight - playerHeight.toInt()) }
                .size(64.dp)
        )

        // HUD
        Column(Modifier.padding(16.dp)) {
            Text("HP: $hp", color = Color.White)
            Text("Score: $score", color = Color.White)
        }
    }
}


data class Bullet(
    var x: Float,
    var y: Float,
    var speed: Float
)

data class Enemy(
    val x: Float,
    val y: Animatable<Float, AnimationVector1D>,
    val drawable: Int,
    var health: Int,
    val speed: Float = 5f
)














