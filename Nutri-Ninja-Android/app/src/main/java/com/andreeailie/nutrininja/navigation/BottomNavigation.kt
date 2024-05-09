package com.andreeailie.nutrininja.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.andreeailie.core.navigation.BottomNavItem
import com.andreeailie.core_ui.BackgroundDarkGreen
import com.andreeailie.core_ui.BackgroundNavigationGreen
import com.andreeailie.core_ui.LocalSpacing

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Groceries,
        BottomNavItem.Profile
    )
    val spacing = LocalSpacing.current
    Box(
        modifier = Modifier
            .padding(
                horizontal = spacing.spaceMedium,
            )
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .background(BackgroundNavigationGreen)
    ) {
        BottomNavigation(
            modifier = Modifier
                .padding(horizontal = spacing.spaceExtraSmall, vertical = spacing.spaceExtraSmall)
                .fillMaxWidth(),
            backgroundColor = BackgroundNavigationGreen,
            contentColor = Color.Gray,
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            items.forEach { item ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painterResource(id = item.icon),
                            contentDescription = item.title,
                            modifier = Modifier.size(35.dp)
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = if (currentRoute == item.screenRoute) Color.White else Color.DarkGray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.DarkGray.copy(0.4f),
                    alwaysShowLabel = true,
                    selected = currentRoute == item.screenRoute,
                    onClick = {
                        navController.navigate(item.screenRoute) {
                            navController.graph.startDestinationRoute?.let { screenRoute ->
                                popUpTo(screenRoute) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier
                        .background(
                            if (currentRoute == item.screenRoute) BackgroundDarkGreen else Color.White,
                            shape = RoundedCornerShape(70.dp)
                        )
                        .padding(5.dp)
                )
            }
        }
    }
}