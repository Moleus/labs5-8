package ru.moleus.kollector.ui.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

private val DarkColorPalette = darkColors(
    primary = cyan,
    onPrimary = gray900,
    background = gray900,
    onBackground = white,
    surface = gray700,
    onSurface = white
)

private val LightColorPalette = lightColors(
    primary = lightBlue,
    primaryVariant = oceanBlue,
    onPrimary = gray900,
    background = white,
    onBackground = gray900,
    surface = darkBlue_,
    onSurface = gray900
)

@Composable
fun MyTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
//        DarkColorPalette
        LightColorPalette
    } else {
        LightColorPalette
    }

    val customColors = if (darkTheme) LightCustomColors else DarkCustomColors

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes
    ) {
        CompositionLocalProvider(
            LocalCustomColors provides customColors,
            LocalContentColor provides MaterialTheme.colors.onBackground,
        ) {
            content()
        }
    }
}

object MyTheme {
    val customColors: CustomColors
        @Composable
        @ReadOnlyComposable
        get() = LocalCustomColors.current
}