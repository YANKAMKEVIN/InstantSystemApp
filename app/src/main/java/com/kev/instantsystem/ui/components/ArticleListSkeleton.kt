package com.kev.instantsystem.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kev.instantsystem.R
import com.kev.instantsystem.ui.theme.ISColors

/**
 * Pulsing placeholder shaped like a news list (featured article, then rows) shown while the
 * first page loads, so the screen already has its structure.
 */
@Composable
fun ArticleListSkeleton(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "skeleton")
    val pulse by transition.animateFloat(
        initialValue = 0.45f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 800), RepeatMode.Reverse),
        label = "skeletonAlpha"
    )
    val blockColor = MaterialTheme.ISColors.Neutral._700
    val loadingDescription = stringResource(R.string.loading)

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("LoaderIndicator")
            .semantics { contentDescription = loadingDescription }
            .alpha(pulse)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(blockColor)
        )
        repeat(5) {
            SkeletonRow(color = blockColor)
        }
    }
}

@Composable
private fun SkeletonRow(color: Color) {
    Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Box(
            modifier = Modifier
                .padding(end = 8.dp)
                .size(48.dp)
                .background(color, CircleShape)
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SkeletonBar(color = color, widthFraction = 0.9f)
            SkeletonBar(color = color, widthFraction = 0.6f)
        }
    }
}

@Composable
private fun SkeletonBar(color: Color, widthFraction: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth(widthFraction)
            .height(14.dp)
            .background(color, RoundedCornerShape(4.dp))
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PreviewArticleListSkeleton() {
    ArticleListSkeleton()
}
