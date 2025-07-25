package com.kev.instantsystem.ui.theme

import androidx.compose.ui.graphics.Color

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf

val Purple200 = Color(0xFFEEECF5)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)

class ISColors {
    val Brand = Brand()
    val Neutral = Neutral()
    val Gray = Gray()
    val Success = Success()
    val Warning = Warning()
    val Danger = Danger()
    val AccentBlue = AccentBlue()
    val AccentYellow = AccentYellow()
    val AccentPink = AccentPink()
    val Gradients = Gradients()
}

val LocaleISColors = compositionLocalOf { ISColors() }

val MaterialTheme.ISColors: ISColors
    @Composable
    @ReadOnlyComposable
    get() = LocaleISColors.current

class Brand {
    val _100 = Color(0xFFF5F8FF)
    val _200: Color = Color(0xFFD6E4FF)
    val _300: Color = Color(0xFFA3C3FF)
    val _400: Color = Color(0xFF70A2FF)
    val _500: Color = Color(0xFF3B7FFF)
    val _600: Color = Color(0xFF0A5FFF)
    val _700: Color = Color(0xFF004AD6)
    val _800: Color = Color(0xFF0039A3)
    val _900: Color = Color(0xFF002770)
}

class Neutral {
    val _000: Color = Color(0xFFFAFBFD)
    val _100: Color = Color(0xFFEBEFF2)
    val _200: Color = Color(0xFFD7DADE)
    val _300: Color = Color(0xFFA5A7AA)
    val _400: Color = Color(0xFF787D85)
    val _500: Color = Color(0xFF44464B)
    val _600: Color = Color(0xFF38393B)
    val _700: Color = Color(0xFF232426)
    val _800: Color = Color(0xFF15171C)
}

class Gray {
    val _100: Color = Color(0xFFF4F6F9)
    val _200: Color = Color(0xFFE7ECF1)
    val _300: Color = Color(0xFFC4CFDA)
    val _400: Color = Color(0xFFA9B7C7)
    val _500: Color = Color(0xFF8091A3)
    val _600: Color = Color(0xFF616D7A)
    val _700: Color = Color(0xFF384047)
}

class Success {
    val _300: Color = Color(0xFFC2FFEC)
    val _400: Color = Color(0xFF74F3CB)
    val _500: Color = Color(0xFF2DC595)
    val _600: Color = Color(0xFF1F8967)
    val _700: Color = Color(0xFF104736)
}

class Warning {
    val _200: Color = Color(0xFFFAEAE1)
    val _300: Color = Color(0xFFFCD5C0)
    val _400: Color = Color(0xFFFB925A)
    val _500: Color = Color(0xFFFB620E)
    val _600: Color = Color(0xFFDE5104)
    val _700: Color = Color(0xFFB74303)
}

class Danger {
    val _100: Color = Color(0xFFFDEAE8)
    val _200: Color = Color(0xFFFFC1BB)
    val _300: Color = Color(0xFFFF9990)
    val _400: Color = Color(0xFFFD4A60)
    val _500: Color = Color(0xFFE50019)
    val _600: Color = Color(0xFFAD0014)
    val _700: Color = Color(0xFF82000F)
}

class AccentBlue {
    val _200: Color = Color(0xFFE6E9FA)
    val _300: Color = Color(0xFFB5C0FA)
    val _400: Color = Color(0xFF6077F7)
    val _500: Color = Color(0xFF0048E5)
    val _600: Color = Color(0xFF002C8C)
}

class AccentYellow {
    val _300: Color = Color(0xFFFAF1DC)
    val _400: Color = Color(0xFFFAE5B4)
    val _500: Color = Color(0xFFFFC33B)
    val _600: Color = Color(0xFF8C6303)
}

class AccentPink {
    val _400: Color = Color(0xFFFADEE5)
    val _500: Color = Color(0xFFF4B6C4)
    val _600: Color = Color(0xFFC77F90)
    val _700: Color = Color(0xFF5C202E)
}

data class Gradients(
    val Warning: Pair<Color, Color> = Color(0xFFFE6C67) to Color(0xFFCC3F41),
    val Mym: Pair<Color, Color> = Color(0xFF767AEF) to Color(0xFFFF9460),
    val Creator: Pair<Color, Color> = Color(0xFF767AEF) to Color(0xFF41C59B),
    val User: Pair<Color, Color> = Color(0xFFF4B6C4) to Color(0xFFFFD960),
)