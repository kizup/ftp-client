package ru.kizapp.ftpclient.ui.theme.color

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class FrontEndColors(
    val isLight: Boolean,
    // Text Colors
    val textPrimary: Color,
    val textSecondary: Color,
    val textMinor: Color,
    val textInvert: Color,

    // Background Colors
    val backgroundPrimary: Color,
    val backgroundSecondary: Color,
    val backgroundAccent: Color,

    // Control Colors
    val controlPrimary: Color,
    val controlSecondary: Color,
    val controlMinor: Color,
    val controlAccent: Color,
    val controlPrimaryInvert: Color,
    val controlSecondaryInvert: Color,
    val controlMinorInvert: Color,

    //Pressed
    val pressedPrimary: Color,
    val pressedAccent: Color,
    val pressedSecondary: Color,
    val pressedMinor: Color,
    val pressedSecondaryInvert: Color,
    val pressedMinorInvert: Color,

    // Success Colors
    val successPrimary: Color,
    val successDark: Color,
    val successLight: Color,

    // Accent Colors
    val accentPrimary: Color,
    val accentDark: Color,
    val accentLight: Color,

    // Attention Colors
    val attentionPrimary: Color,
    val attentionDark: Color,
    val attentionLight: Color,

    // Brand Colors
    val brandPrimary: Color,
    val brandMinor: Color,
    val brandInvert: Color,

    //Secondary
    val secondaryGreenPrimary: Color,
    val secondaryGreenDark: Color,
    val secondaryGreenLight: Color,

    val secondaryBluePrimary: Color,
    val secondaryBlueDark: Color,
    val secondaryBlueLight: Color,

    val secondaryOrangePrimary: Color,
    val secondaryOrangeDark: Color,
    val secondaryOrangeLight: Color,

    val secondaryPurplePrimary: Color,
    val secondaryPurpleDark: Color,
    val secondaryPurpleLight: Color,

    val secondaryYellowPrimary: Color,
    val secondaryYellowDark: Color,
    val secondaryYellowLight: Color,

    val secondaryWhite: Color,
    val secondaryBlack: Color
) {
    @Composable
    fun toColorScheme(): ColorScheme {
        return MaterialTheme.colorScheme.copy(
            primary = brandPrimary,
            onPrimary = accentPrimary,
        )
    }
}

val LocalFrontEndColors = staticCompositionLocalOf<FrontEndColors> {
    error("No colors provided")
}
