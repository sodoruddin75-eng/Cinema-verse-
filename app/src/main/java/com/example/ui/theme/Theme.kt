package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = MovieVerseCyan,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF083344),
    onPrimaryContainer = Color(0xFFA5F3FC),
    secondary = MovieVerseGold,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF451A03),
    onSecondaryContainer = Color(0xFFFEF3C7),
    tertiary = MovieVersePurple,
    onTertiary = Color.White,
    background = MovieVerseDarkBg,
    onBackground = MovieVerseLightText,
    surface = MovieVerseDarkSurface,
    onSurface = MovieVerseLightText,
    surfaceVariant = MovieVerseDarkCard,
    onSurfaceVariant = MovieVerseSlateText,
    outline = MovieVerseBorder,
    error = MovieVerseRed
)

private val LightColorScheme = lightColorScheme(
    primary = MovieVerseLightPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = Color(0xFF0369A1),
    secondary = MovieVerseGold,
    onSecondary = Color.Black,
    tertiary = MovieVersePurple,
    background = MovieVerseLightBg,
    onBackground = MovieVerseLightTextPrimary,
    surface = MovieVerseLightSurface,
    onSurface = MovieVerseLightTextPrimary,
    surfaceVariant = MovieVerseLightCard,
    onSurfaceVariant = MovieVerseLightTextSecondary,
    outline = Color(0xFFCBD5E1),
    error = MovieVerseRed
)

@Composable
fun MovieVerseTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
