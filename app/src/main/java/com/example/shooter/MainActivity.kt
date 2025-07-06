package com.example.shooter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.shooter.ui.theme.navigation.AppNavigation
import com.example.shooter.ui.theme.ShooterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShooterTheme {
                AppNavigation()
            }
        }
    }
}
