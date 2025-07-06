package com.example.shooter.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HistoryScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Вы — бывший спецагент, которого объявили психически нестабильным и изолировали в секретной клинике. Но вы сбежали. Реальность и галлюцинации переплетаются. Рептилоиды заполонили коридоры, но так ли это на самом деле? Или вы лишь часть чужого эксперимента?.. Ваша цель — выжить, уничтожая врагов, и добраться до правды.",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Назад")
        }
    }
}
