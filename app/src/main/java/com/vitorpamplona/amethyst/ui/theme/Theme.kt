package com.vitorpamplona.amethyst.ui.theme

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.halilibo.richtext.ui.RichTextStyle
import com.halilibo.richtext.ui.resolveDefaults
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.core.DefaultColors
import com.vitorpamplona.amethyst.ui.screen.ThemeViewModel

private val DarkColorPalette = darkColorScheme(
    primary = Purple200,
    secondary = Teal200,
    // secondary = Purple700,
    tertiary = Teal200
)

private val LightColorPalette = lightColorScheme(
    primary = Purple500,
    secondary = Teal200,
    // secondary = Purple700,
    tertiary = Teal200
)

private val DarkNewItemBackground = DarkColorPalette.primary.copy(0.12f)
private val LightNewItemBackground = LightColorPalette.primary.copy(0.12f)

private val DarkSelectedNote = DarkNewItemBackground.compositeOver(DarkColorPalette.background)
private val LightSelectedNote = LightNewItemBackground.compositeOver(LightColorPalette.background)

private val DarkButtonBackground = DarkColorPalette.primary.copy(alpha = 0.32f).compositeOver(DarkColorPalette.background)
private val LightButtonBackground = LightColorPalette.primary.copy(alpha = 0.32f).compositeOver(LightColorPalette.background)

private val DarkLessImportantLink = DarkColorPalette.primary.copy(alpha = 0.52f)
private val LightLessImportantLink = LightColorPalette.primary.copy(alpha = 0.52f)

private val DarkMediumImportantLink = DarkColorPalette.primary.copy(alpha = 0.32f)
private val LightMediumImportantLink = LightColorPalette.primary.copy(alpha = 0.32f)

private val DarkVeryImportantLink = DarkColorPalette.primary.copy(alpha = 0.12f)
private val LightVeryImportantLink = LightColorPalette.primary.copy(alpha = 0.12f)

private val DarkGrayText = DarkColorPalette.onSurface.copy(alpha = 0.52f)
private val LightGrayText = LightColorPalette.onSurface.copy(alpha = 0.52f)

private val DarkPlaceholderText = DarkColorPalette.onSurface.copy(alpha = 0.32f)
private val LightPlaceholderText = LightColorPalette.onSurface.copy(alpha = 0.32f)

private val DarkPlaceholderTextColorFilter = ColorFilter.tint(DarkPlaceholderText)
private val LightPlaceholderTextColorFilter = ColorFilter.tint(LightPlaceholderText)

private val DarkOnBackgroundColorFilter = ColorFilter.tint(DarkColorPalette.onBackground)
private val LightOnBackgroundColorFilter = ColorFilter.tint(LightColorPalette.onBackground)

private val DarkSubtleButton = DarkColorPalette.onSurface.copy(alpha = 0.22f)
private val LightSubtleButton = LightColorPalette.onSurface.copy(alpha = 0.22f)

private val DarkSubtleBorder = DarkColorPalette.onSurface.copy(alpha = 0.12f)
private val LightSubtleBorder = LightColorPalette.onSurface.copy(alpha = 0.12f)

private val DarkReplyItemBackground = DarkColorPalette.onSurface.copy(alpha = 0.05f)
private val LightReplyItemBackground = LightColorPalette.onSurface.copy(alpha = 0.05f)

private val DarkZapraiserBackground = BitcoinOrange.copy(0.52f).compositeOver(DarkColorPalette.background)
private val LightZapraiserBackground = BitcoinOrange.copy(0.52f).compositeOver(LightColorPalette.background)

private val DarkOverPictureBackground = DarkColorPalette.background.copy(0.62f)
private val LightOverPictureBackground = LightColorPalette.background.copy(0.62f)

val RepostPictureBorderDark = Modifier.border(
    2.dp,
    DarkColorPalette.background,
    CircleShape
)

val RepostPictureBorderLight = Modifier.border(
    2.dp,
    LightColorPalette.background,
    CircleShape
)

val DarkImageModifier = Modifier
    .fillMaxWidth()
    .clip(shape = QuoteBorder)
    .border(
        1.dp,
        DarkSubtleBorder,
        QuoteBorder
    )

val LightImageModifier = Modifier
    .fillMaxWidth()
    .clip(shape = QuoteBorder)
    .border(
        1.dp,
        LightSubtleBorder,
        QuoteBorder
    )

val DarkProfile35dpModifier = Modifier
    .size(Size35dp)
    .clip(shape = CircleShape)

val LightProfile35dpModifier = Modifier
    .fillMaxWidth()
    .clip(shape = CircleShape)

val DarkReplyBorderModifier = Modifier
    .padding(top = 5.dp)
    .fillMaxWidth()
    .clip(shape = QuoteBorder)
    .border(
        1.dp,
        DarkSubtleBorder,
        QuoteBorder
    )

val LightReplyBorderModifier = Modifier
    .padding(top = 2.dp, bottom = 0.dp, start = 0.dp, end = 0.dp)
    .fillMaxWidth()
    .clip(shape = QuoteBorder)
    .border(
        1.dp,
        LightSubtleBorder,
        QuoteBorder
    )

val DarkInnerPostBorderModifier = Modifier
    .padding(top = 5.dp)
    .fillMaxWidth()
    .clip(shape = QuoteBorder)
    .border(
        1.dp,
        DarkSubtleBorder,
        QuoteBorder
    )

val LightInnerPostBorderModifier = Modifier
    .padding(top = 5.dp)
    .fillMaxWidth()
    .clip(shape = QuoteBorder)
    .border(
        1.dp,
        LightSubtleBorder,
        QuoteBorder
    )

val RichTextDefaults = RichTextStyle().resolveDefaults()

val MarkDownStyleOnDark = RichTextDefaults.copy(
    paragraphSpacing = DefaultParagraphSpacing,
    headingStyle = DefaultHeadingStyle,
    codeBlockStyle = RichTextDefaults.codeBlockStyle?.copy(
        textStyle = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontSize = Font14SP
        ),
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth()
            .clip(shape = QuoteBorder)
            .border(
                1.dp,
                DarkSubtleBorder,
                QuoteBorder
            )
            .background(DarkColorPalette.onSurface.copy(alpha = 0.05f))
    ),
    stringStyle = RichTextDefaults.stringStyle?.copy(
        linkStyle = SpanStyle(
            color = DarkColorPalette.primary
        ),
        codeStyle = SpanStyle(
            fontFamily = FontFamily.Monospace,
            fontSize = Font14SP,
            background = DarkColorPalette.onSurface.copy(alpha = 0.22f)
        )
    )
)

val MarkDownStyleOnLight = RichTextDefaults.copy(
    paragraphSpacing = DefaultParagraphSpacing,
    headingStyle = DefaultHeadingStyle,
    codeBlockStyle = RichTextDefaults.codeBlockStyle?.copy(
        textStyle = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontSize = Font14SP
        ),
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth()
            .clip(shape = QuoteBorder)
            .border(
                1.dp,
                LightSubtleBorder,
                QuoteBorder
            )
            .background(DarkColorPalette.onSurface.copy(alpha = 0.05f))
    ),
    stringStyle = RichTextDefaults.stringStyle?.copy(
        linkStyle = SpanStyle(
            color = LightColorPalette.primary
        ),
        codeStyle = SpanStyle(
            fontFamily = FontFamily.Monospace,
            fontSize = Font14SP,
            background = LightColorPalette.onSurface.copy(alpha = 0.22f)
        )
    )
)

val ColorScheme.isLight: Boolean
    get() = primary == Purple500

val ColorScheme.newItemBackgroundColor: Color
    get() = if (isLight) LightNewItemBackground else DarkNewItemBackground

val ColorScheme.replyBackground: Color
    get() = if (isLight) LightReplyItemBackground else DarkReplyItemBackground

val ColorScheme.selectedNote: Color
    get() = if (isLight) LightSelectedNote else DarkSelectedNote

val ColorScheme.secondaryButtonBackground: Color
    get() = if (isLight) LightButtonBackground else DarkButtonBackground

val ColorScheme.lessImportantLink: Color
    get() = if (isLight) LightLessImportantLink else DarkLessImportantLink

val ColorScheme.zapraiserBackground: Color
    get() = if (isLight) LightZapraiserBackground else DarkZapraiserBackground

val ColorScheme.mediumImportanceLink: Color
    get() = if (isLight) LightMediumImportantLink else DarkMediumImportantLink
val ColorScheme.veryImportantLink: Color
    get() = if (isLight) LightVeryImportantLink else DarkVeryImportantLink

val ColorScheme.placeholderText: Color
    get() = if (isLight) LightPlaceholderText else DarkPlaceholderText

val ColorScheme.nip05: Color
    get() = if (isLight) Nip05EmailColorLight else Nip05EmailColorDark

val ColorScheme.placeholderTextColorFilter: ColorFilter
    get() = if (isLight) LightPlaceholderTextColorFilter else DarkPlaceholderTextColorFilter

val ColorScheme.onBackgroundColorFilter: ColorFilter
    get() = if (isLight) LightOnBackgroundColorFilter else DarkOnBackgroundColorFilter

val ColorScheme.grayText: Color
    get() = if (isLight) LightGrayText else DarkGrayText

val ColorScheme.subtleBorder: Color
    get() = if (isLight) LightSubtleBorder else DarkSubtleBorder

val ColorScheme.subtleButton: Color
    get() = if (isLight) LightSubtleButton else DarkSubtleButton

val ColorScheme.overPictureBackground: Color
    get() = if (isLight) LightOverPictureBackground else DarkOverPictureBackground

val ColorScheme.bitcoinColor: Color
    get() = if (isLight) BitcoinLight else BitcoinDark

val ColorScheme.warningColor: Color
    get() = if (isLight) LightWarningColor else DarkWarningColor

val ColorScheme.allGoodColor: Color
    get() = if (isLight) LightAllGoodColor else DarkAllGoodColor

val ColorScheme.markdownStyle: RichTextStyle
    get() = if (isLight) MarkDownStyleOnLight else MarkDownStyleOnDark

val ColorScheme.repostProfileBorder: Modifier
    get() = if (isLight) RepostPictureBorderLight else RepostPictureBorderDark

val ColorScheme.imageModifier: Modifier
    get() = if (isLight) LightImageModifier else DarkImageModifier

val ColorScheme.profile35dpModifier: Modifier
    get() = if (isLight) LightProfile35dpModifier else DarkProfile35dpModifier

val ColorScheme.replyModifier: Modifier
    get() = if (isLight) LightReplyBorderModifier else DarkReplyBorderModifier

val ColorScheme.innerPostModifier: Modifier
    get() = if (isLight) LightInnerPostBorderModifier else DarkInnerPostBorderModifier

val ColorScheme.chartStyle: ChartStyle
    get() {
        val defaultColors = if (isLight) DefaultColors.Light else DefaultColors.Dark
        return ChartStyle.fromColors(
            axisLabelColor = Color(defaultColors.axisLabelColor),
            axisGuidelineColor = Color(defaultColors.axisGuidelineColor),
            axisLineColor = Color(defaultColors.axisLineColor),
            entityColors = listOf(
                defaultColors.entity1Color,
                defaultColors.entity2Color,
                defaultColors.entity3Color
            ).map(::Color),
            elevationOverlayColor = Color(defaultColors.elevationOverlayColor)
        )
    }

@Composable
fun AmethystTheme(themeViewModel: ThemeViewModel, content: @Composable () -> Unit) {
    val theme = themeViewModel.theme.observeAsState()
    val darkTheme = when (theme.value) {
        2 -> true
        1 -> false
        else -> isSystemInDarkTheme()
    }
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            if (darkTheme) {
                window.statusBarColor = colors.background.toArgb()
            } else {
                window.statusBarColor = colors.primary.toArgb()
            }
            window.navigationBarColor = colors.background.toArgb()
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }
}
