package com.example.shooter.ui.theme.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ShooterLogo() {
    Text(
        text = "Escape from Sector‑13",
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold
    )
}
