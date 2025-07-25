package com.kev.instantsystem.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Spacing(
    val xxs: Dp = 4.dp,
    val xs: Dp = 8.dp,
    val s: Dp = 10.dp,
    val sm: Dp = 12.dp,
    val m: Dp = 16.dp,
    val lg1: Dp = 20.dp,
    val lg2: Dp = 24.dp,
    val lg3: Dp = 32.dp,
    val lg4: Dp = 40.dp,
    val xlg: Dp = 48.dp,
    val xxlg: Dp = 64.dp,
)

val LocaleSpacing = compositionLocalOf { Spacing() }

val MaterialTheme.spacing: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocaleSpacing.current