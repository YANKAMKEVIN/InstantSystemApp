package com.kev.instantsystem.ui.utils

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun SpacerVertical(dim: Dp) {
    Spacer(Modifier.height(dim))
}

@Composable
fun SpacerHorizontal(dim: Dp) {
    Spacer(Modifier.width(dim))
}