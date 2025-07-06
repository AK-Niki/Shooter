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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.shooter.ui.theme.model.Bullet
import com.example.shooter.ui.theme.model.Enemy
import com.example.shooter.ui.theme.utils.SoundManager
import com.example.shooter.ui.theme.viewmodel.GameViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.gestures.detectHorizontalDragGestures

@Composable
fun GameScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel = remember { GameViewModel(context) }
    val scope = rememberCoroutineScope()

    val playerX = viewModel.playerX
    val bullets = viewModel.bullets
    val enemies = viewModel.enemies
    val score by viewModel.score.collectAsState()
    val hp by viewModel.hp.collectAsState()
    val isGameOver by viewModel.isGameOver.collectAsState()

    var screenWidth by remember { mutableStateOf(0) }
    var screenHeight by remember { mutableStateOf(0) }

    LaunchedEffect(screenWidth) {
        viewModel.init(screenWidth)
    }

    LaunchedEffect(true) {
        viewModel.startSpawningEnemies()
        viewModel.startShooting(screenHeight)
        viewModel.handleCollisions(
            screenHeight = screenHeight,
            onGameOver = { navController.navigate("gameover") }
        )
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
                        viewModel.movePlayer(dragAmount, screenWidth)
                    }
                }
            }
    ) {
        // Фон
        Image(
            painter = painterResource(id = R.drawable.floor),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        // Враги
        enemies.forEach { enemy ->
            Image(
                painter = painterResource(enemy.drawable),
                contentDescription = null,
                modifier = Modifier
                    .offset { IntOffset(enemy.x.toInt(), enemy.y.value.toInt()) }
                    .size(64.dp)
            )
        }

        // Пули
        bullets.forEach { bullet ->
            Box(
                modifier = Modifier
                    .offset { IntOffset(bullet.x.toInt(), bullet.y.toInt()) }
                    .size(8.dp, 16.dp)
                    .background(Color.Yellow)
            )
        }

        // Игрок
        Image(
            painter = painterResource(id = R.drawable.player),
            contentDescription = null,
            modifier = Modifier
                .offset { IntOffset(playerX.value.toInt(), screenHeight - 64) }
                .size(64.dp)
        )

        // HUD
        Column(modifier = Modifier.padding(16.dp)) {
            Text("HP: $hp", color = Color.White)
            Text("Score: $score", color = Color.White)
        }
    }
}













