package com.alves.cestabasica.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AlvesGreen = Color(0xFF2E7D32)
private val AlvesGreenDark = Color(0xFF1B5E20)
private val AlvesAmber = Color(0xFFF9A825)
private val AlvesRed = Color(0xFFC62828)

private val LightColors = lightColorScheme(
    primary = AlvesGreen,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFA5D6A7),
    onPrimaryContainer = AlvesGreenDark,
    secondary = AlvesAmber,
    onSecondary = Color.Black,
    error = AlvesRed
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF81C784),
    onPrimary = Color.Black,
    primaryContainer = AlvesGreenDark,
    onPrimaryContainer = Color(0xFFC8E6C9),
    secondary = AlvesAmber,
    onSecondary = Color.Black,
    error = Color(0xFFEF9A9A)
)

@Composable
fun CestaBasicaAlvesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
