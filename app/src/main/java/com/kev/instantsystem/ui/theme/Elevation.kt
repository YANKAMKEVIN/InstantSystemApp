package com.kev.instantsystem.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Elevation(
    val _01: Dp = 2.dp,
    val _02: Dp = 4.dp,
    val _03: Dp = 8.dp,
    val _04: Dp = 16.dp,
)

val LocaleElevation = compositionLocalOf { Elevation() }

val MaterialTheme.elevation : Elevation
    @Composable
    @ReadOnlyComposable
    get() = LocaleElevation.current