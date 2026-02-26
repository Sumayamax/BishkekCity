package com.example.myapplication.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary            = Teal40,
    onPrimary          = Color.White,
    primaryContainer   = Teal90,
    onPrimaryContainer = Teal10,
    secondary            = Amber40,
    onSecondary          = Color.White,
    secondaryContainer   = Amber90,
    onSecondaryContainer = Amber10,
    tertiary            = Green40,
    onTertiary          = Color.White,
    tertiaryContainer   = Green90,
    onTertiaryContainer = Green10,
    background      = Neutral99,
    onBackground    = Teal10,
    surface         = Neutral99,
    onSurface       = Teal10,
    surfaceVariant    = NeutralVar90,
    onSurfaceVariant  = NeutralVar30,
    outline           = Color(0xFF6D8083),
)

private val DarkColorScheme = darkColorScheme(
    primary            = Teal80,
    onPrimary          = Teal20,
    primaryContainer   = Teal30,
    onPrimaryContainer = Teal90,
    secondary            = Amber80,
    onSecondary          = Amber20,
    secondaryContainer   = Amber40,
    onSecondaryContainer = Amber90,
    tertiary            = Green80,
    onTertiary          = Green10,
    tertiaryContainer   = Green30,
    onTertiaryContainer = Green90,
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
