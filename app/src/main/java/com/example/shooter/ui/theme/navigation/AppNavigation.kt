package com.example.shooter.ui.theme.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shooter.ui.theme.screens.GameOverScreen
import com.example.shooter.ui.theme.screens.GameScreen
import com.example.shooter.ui.theme.screens.HistoryScreen
import com.example.shooter.ui.theme.screens.MenuScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "menu") {
        composable("menu") {
            MenuScreen(navController)
        }
        composable("history") {
            HistoryScreen(navController)
        }
        composable("game") {
            GameScreen(navController)
        }
        composable("gameover") {
            GameOverScreen(navController)
        }
    }
}
