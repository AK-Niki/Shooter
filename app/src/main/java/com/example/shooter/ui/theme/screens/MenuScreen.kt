package com.example.shooter.ui.screens

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.shooter.R
import androidx.compose.foundation.Image

@Composable
fun MenuScreen(navController: NavController) {
    val activity = LocalActivity.current

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Фон-картинка
        Image(
            painter = painterResource(R.drawable.menu_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Содержимое поверх
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "SHOOTER",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.navigate("game") },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Начать игру")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("history") },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("История")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { activity?.finish() },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Выход")
            }
        }
    }
}
