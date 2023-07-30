package ru.kizapp.ftpclient.ui.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import ru.kizapp.ftpclient.ui.theme.color.FrontEndColors
import ru.kizapp.ftpclient.ui.theme.color.LocalFrontEndColors
import ru.kizapp.ftpclient.ui.theme.color.lightPalette
import ru.kizapp.ftpclient.ui.theme.typography.Body
import ru.kizapp.ftpclient.ui.theme.typography.FrontEndTypography
import ru.kizapp.ftpclient.ui.theme.typography.LocalFrontEndTypography
import ru.kizapp.ftpclient.ui.theme.typography.bodyLarge
import ru.kizapp.ftpclient.ui.theme.typography.bodyMedium
import ru.kizapp.ftpclient.ui.theme.typography.bodySmall
import ru.kizapp.ftpclient.ui.theme.typography.headings
import ru.kizapp.ftpclient.ui.theme.typography.minor

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
background = Color(0xFFFFFBFE),
surface = Color(0xFFFFFBFE),
onPrimary = Color.White,
onSecondary = Color.White,
onTertiary = Color.White,
onBackground = Color(0xFF1C1B1F),
onSurface = Color(0xFF1C1B1F),
*/
)

@Composable
fun FrontEndTheme(
    content: @Composable () -> Unit
) {
    val typography = FrontEndTypography(
        headings = headings,
        body = Body(
            large = bodyLarge,
            medium = bodyMedium,
            small = bodySmall,
        ),
        minor = minor,
    )
    val palette = lightPalette
    val rippleIndicator = rememberRipple()
    MaterialTheme(
        colorScheme = palette.toColorScheme(),
    ) {
        CompositionLocalProvider(
            LocalFrontEndColors provides palette,
            LocalFrontEndTypography provides typography,
            LocalIndication provides rippleIndicator,
            content = content
        )
    }
}

object FrontEnd {
    val color: FrontEndColors
        @Composable
        get() = LocalFrontEndColors.current

    val typography: FrontEndTypography
        @Composable
        get() = LocalFrontEndTypography.current
}
