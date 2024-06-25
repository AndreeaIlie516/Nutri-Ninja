package com.andreeailie.nutrininja.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.andreeailie.core.navigation.BottomNavItem
import com.andreeailie.tracker_presentation.groceries_list.GroceriesListScreen
import com.andreeailie.tracker_presentation.groceries_list.GroceryListViewModel
import com.andreeailie.tracker_presentation.tracker_overview.TrackerOverviewScreen
import com.andreeailie.tracker_presentation.tracker_overview.TrackerOverviewViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController,
    trackerOverviewViewModel: TrackerOverviewViewModel,
    groceryListViewModel: GroceryListViewModel
) {
    NavHost(navController, startDestination = BottomNavItem.Home.screenRoute) {
        composable(BottomNavItem.Home.screenRoute) {
            TrackerOverviewScreen(
                onNavigate = { navController.navigate(it.route) },
                viewModel = trackerOverviewViewModel
            )
        }
        composable(BottomNavItem.Groceries.screenRoute) {
            GroceriesListScreen(
                onNavigate = { navController.navigate(it.route) },
                viewModel = groceryListViewModel
            )
        }
        composable(BottomNavItem.Profile.screenRoute) {
        }
    }
}