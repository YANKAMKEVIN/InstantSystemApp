package com.kev.instantsystem.ui.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kev.instantsystem.R
import com.kev.instantsystem.ui.navigation.BottomNavItem.Companion.allItems
import com.kev.instantsystem.ui.theme.ISColors

@Composable
fun MainBottomBar(currentRoute: String?, onItemSelected: (BottomNavItem) -> Unit) {
    val activeColor = MaterialTheme.ISColors.Brand._500
    val inactiveColor = MaterialTheme.ISColors.Neutral._400

    NavigationBar(containerColor = MaterialTheme.colorScheme.background) {

        allItems.forEach { item ->
            val label = stringResource(item.labelRes)
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemSelected(item) },
                icon = {
                    // No tint here: the item colours below handle selected / unselected.
                    val iconRes = if (isSelected) item.selectedIconRes ?: item.iconRes else item.iconRes
                    if (iconRes != null) {
                        Icon(
                            painter = painterResource(id = iconRes),
                            modifier = Modifier.size(if (iconRes == R.drawable.headlines_logo) 26.dp else 24.dp),
                            contentDescription = null
                        )
                    } else {
                        item.defaultIcon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = null
                            )
                        }
                    }
                },
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            // Let the item colours apply (the theme styles carry a fixed colour).
                            color = Color.Unspecified
                        ),
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = activeColor,
                    unselectedIconColor = inactiveColor,
                    selectedTextColor = activeColor,
                    unselectedTextColor = inactiveColor,
                    indicatorColor = activeColor.copy(alpha = 0.16f)
                )
            )
        }
    }
}
