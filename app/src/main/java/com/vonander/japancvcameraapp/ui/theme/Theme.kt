package com.vonander.japancvcameraapp.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = BlueGray600,
    primaryVariant = BlueGray900,
    secondary = Teal300,
    background = BlueGray900,
    surface = BlueGray900,
    onPrimary = Color.White,
    onBackground = BlueGray400
)

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    primary = BlueGray400,
    primaryVariant = BlueGray600,
    secondary = Teal300,
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onBackground = BlueGray900

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun JapanCVCameraAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}