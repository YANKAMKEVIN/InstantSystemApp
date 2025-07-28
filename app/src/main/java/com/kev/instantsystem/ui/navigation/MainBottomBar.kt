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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kev.instantsystem.ui.navigation.BottomNavItem.Companion.allItems

@Composable
fun MainBottomBar(currentRoute: String?, onItemSelected: (BottomNavItem) -> Unit) {

    NavigationBar(containerColor = MaterialTheme.colorScheme.background) {

        allItems.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemSelected(item) },
                icon = {
                    if (item.iconRes != null) {
                        Icon(
                            painter = painterResource(id = item.iconRes),
                            modifier = Modifier.size(26.dp),
                            contentDescription = item.label,
                            tint = Color.White
                        )
                    } else {
                        item.defaultIcon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = item.label,
                                tint = Color.White
                            )
                        }
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (isSelected) Color.White else Color.Gray
                        ),
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.DarkGray
                )
            )
        }
    }
}
