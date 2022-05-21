package ru.moleus.kollector.ui.compose.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalCustomColors = staticCompositionLocalOf { LightCustomColors }

val LightCustomColors: CustomColors =
    CustomColors(
        success = neonGreen,
        error = red
    )

val DarkCustomColors: CustomColors =
    CustomColors(
        success = neonGreen,
        error = red
    )

class CustomColors(
    val success: Color,
    val error: Color,
)
