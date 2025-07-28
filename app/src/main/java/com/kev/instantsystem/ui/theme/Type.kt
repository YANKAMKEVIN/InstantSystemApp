package com.kev.instantsystem.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.kev.instantsystem.R

val nn_nouvelle_grotesk_std = FontFamily(
    Font(R.font.nn_nouvelle_grotesk_std_black, weight = FontWeight.Black),
    Font(R.font.nn_nouvelle_grotesk_std_light, weight = FontWeight.Light),
    Font(R.font.nn_nouvelle_grotesk_std_normal, weight = FontWeight.Normal),
    Font(R.font.nn_nouvelle_grotesk_std_bold, weight = FontWeight.Bold),
)

val isTypography = ISTypography()

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = nn_nouvelle_grotesk_std,
        fontWeight = FontWeight.Black,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        color = Neutral()._100
    ),
    displayMedium = TextStyle(
        fontFamily = nn_nouvelle_grotesk_std,
        fontWeight = FontWeight.Black,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        color = Neutral()._100
    ),
    displaySmall = TextStyle(
        fontFamily = nn_nouvelle_grotesk_std,
        fontWeight = FontWeight.Black,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        color = Neutral()._100
    ),
    headlineLarge = isTypography.title.h1,
    headlineMedium = isTypography.title.h2,
    headlineSmall = isTypography.title.h3,
    titleLarge = isTypography.title.h3,
    titleMedium = isTypography.title.h4,
    titleSmall = isTypography.title.h5,
    bodyLarge = isTypography.body.large,
    bodyMedium = isTypography.body.medium,
    bodySmall = isTypography.body.indicationUp,
    labelLarge = isTypography.button.large,
    labelMedium = isTypography.button.default,
    labelSmall = isTypography.button.small
)

data class ISTypography(
    val title: Title = Title(),
    val link: Link = Link(),
    val body: Body = Body(),
    val button: Button = Button()
)

data class Title(
    val h1: TextStyle = TextStyle(
        fontFamily = nn_nouvelle_grotesk_std,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        color = Neutral()._100
    ),
    val h2: TextStyle = TextStyle(
        fontFamily = nn_nouvelle_grotesk_std,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        color = Neutral()._100
    ),
    val h3: TextStyle = TextStyle(
        fontFamily = nn_nouvelle_grotesk_std,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 20.sp,
        lineHeight = 25.sp,
        color = Neutral()._100
    ),
    val h4: TextStyle = TextStyle(
        fontFamily = nn_nouvelle_grotesk_std,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        color = Neutral()._100
    ),
    val h5: TextStyle = TextStyle(
        fontFamily = nn_nouvelle_grotesk_std,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 12.sp,
        lineHeight = 15.sp,
        color = Neutral()._100
    )
)

data class Link(
    val link: TextStyle = TextStyle(
        fontFamily = nn_nouvelle_grotesk_std,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = Neutral()._100,
        textDecoration = TextDecoration.Underline
    )
)

data class Body(
    val large: TextStyle = TextStyle(
        fontFamily = nn_nouvelle_grotesk_std,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = Neutral()._100
    ),
    val medium: TextStyle = TextStyle(
        fontFamily = nn_nouvelle_grotesk_std,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = Neutral()._100
    ),
    val indicationUp: TextStyle = TextStyle(
        fontFamily = nn_nouvelle_grotesk_std,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        color = Neutral()._100
    ),
    val caption: TextStyle = TextStyle(
        fontFamily = nn_nouvelle_grotesk_std,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 14.sp,
        color = Neutral()._100,
    ),
    val largeMedium: TextStyle = TextStyle(
        fontFamily = nn_nouvelle_grotesk_std,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = Neutral()._100
    ),
    val defaultMedium: TextStyle = TextStyle(
        fontFamily = nn_nouvelle_grotesk_std,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = Neutral()._100
    )
)

data class Button(
    val large: TextStyle = TextStyle(
        fontFamily = nn_nouvelle_grotesk_std,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        color = Neutral()._100
    ),
    val default: TextStyle = TextStyle(
        fontFamily = nn_nouvelle_grotesk_std,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        color = Neutral()._100
    ),
    val small: TextStyle = TextStyle(
        fontFamily = nn_nouvelle_grotesk_std,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 16.sp,
        color = Neutral()._100
    )
)

val LocaleISTypography = compositionLocalOf { ISTypography() }

val MaterialTheme.ISTypography: ISTypography
    @Composable
    @ReadOnlyComposable
    get() = LocaleISTypography.current