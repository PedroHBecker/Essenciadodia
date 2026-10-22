package com.seugrupo.essenciadodia.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF7A2E55),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFD8E8),
    onPrimaryContainer = Color(0xFF3A0B26),
    secondary = Color(0xFF8A6A1F),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE8A8),
    onSecondaryContainer = Color(0xFF2B1F00),
    background = Color(0xFFFDEFF4),
    onBackground = Color(0xFF211A1D),
    surface = Color(0xFFFDEFF4),
    onSurface = Color(0xFF211A1D),
    surfaceVariant = Color(0xFFF2DEE6),
    onSurfaceVariant = Color(0xFF514349),
    surfaceContainer = Color(0xFFF8DDE8),
    surfaceContainerHighest = Color.White,
    outline = Color(0xFF84737A)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFFB0D2),
    onPrimary = Color(0xFF4A1235),
    primaryContainer = Color(0xFF62294A),
    onPrimaryContainer = Color(0xFFFFD8E8),
    secondary = Color(0xFFE2C26B),
    onSecondary = Color(0xFF3F2E00),
    secondaryContainer = Color(0xFF5C4600),
    onSecondaryContainer = Color(0xFFFFE8A8),
    background = Color(0xFF181114),
    onBackground = Color(0xFFEBDFE2),
    surface = Color(0xFF181114),
    onSurface = Color(0xFFEBDFE2),
    surfaceVariant = Color(0xFF514349),
    onSurfaceVariant = Color(0xFFD6C2C9),
    surfaceContainerHighest = Color(0xFF3A2F34),
    outline = Color(0xFF9F8C93)
)

@Composable
fun EssenciadodiaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}

// Cores da barra do topo, usadas em todas as telas
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun corTopBar(): TopAppBarColors = TopAppBarDefaults.topAppBarColors(
    containerColor = MaterialTheme.colorScheme.primary,
    titleContentColor = MaterialTheme.colorScheme.onPrimary,
    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
)