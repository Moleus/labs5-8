package common.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import ui.theme.neonGreen
import ui.theme.red

val LocalCustomColors = staticCompositionLocalOf { LightCustomColors }

val LightCustomColors: CustomColors =
    CustomColors(
        success = neonGreen,
        custom2 = red
    )

val DarkCustomColors: CustomColors =
    CustomColors(
        success = neonGreen,
        custom2 = red
    )

class CustomColors(
    val success: Color,
    val custom2: Color,
)
