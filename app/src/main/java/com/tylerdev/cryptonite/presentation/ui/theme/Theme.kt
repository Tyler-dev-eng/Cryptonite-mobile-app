package com.tylerdev.cryptonite.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = ElectricTeal,
    onPrimary = NearBlack,
    primaryContainer = ElectricTealContainer,
    onPrimaryContainer = OnElectricTealContainer,
    secondary = SlateBlue,
    onSecondary = NearBlack,
    secondaryContainer = SlateBlueContainer,
    onSecondaryContainer = OnSlateBlueContainer,
    tertiary = AmberAccent,
    onTertiary = NearBlack,
    tertiaryContainer = AmberAccentContainer,
    onTertiaryContainer = OnAmberAccentContainer,
    error = CryptoRed,
    onError = NearBlack,
    errorContainer = CryptoRedContainer,
    onErrorContainer = OnCryptoRedContainer,
    background = NearBlack,
    onBackground = OffWhite,
    surface = DarkSurface,
    onSurface = OffWhite,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = OffWhite,
    outline = DarkOutline
)

private val LightColorScheme = lightColorScheme(
    primary = DeepTeal,
    onPrimary = Color.White,
    primaryContainer = DeepTealContainer,
    onPrimaryContainer = OnDeepTealContainer,
    secondary = DeepSlate,
    onSecondary = Color.White,
    secondaryContainer = DeepSlateContainer,
    onSecondaryContainer = OnDeepSlateContainer,
    tertiary = DeepAmber,
    onTertiary = Color.White,
    tertiaryContainer = DeepAmberContainer,
    onTertiaryContainer = OnDeepAmberContainer,
    error = DeepRed,
    onError = Color.White,
    errorContainer = DeepRedContainer,
    onErrorContainer = OnDeepRedContainer,
    background = LightBackground,
    onBackground = NearBlackText,
    surface = LightSurface,
    onSurface = NearBlackText,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = NearBlackText,
    outline = LightOutline
)

@Composable
fun CryptoniteTheme(
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
