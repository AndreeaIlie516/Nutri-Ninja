package com.andreeailie.nutrininja

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.andreeailie.core.domain.preferences.Preferences
import com.andreeailie.core.navigation.Route
import com.andreeailie.nutrininja.navigation.BottomNavigation
import com.andreeailie.nutrininja.navigation.navigate
import com.andreeailie.nutrininja.ui.theme.NutriNinjaTheme
import com.andreeailie.onboarding_presentation.activity.ActivityScreen
import com.andreeailie.onboarding_presentation.age.AgeScreen
import com.andreeailie.onboarding_presentation.gender.GenderScreen
import com.andreeailie.onboarding_presentation.goal.GoalScreen
import com.andreeailie.onboarding_presentation.height.HeightScreen
import com.andreeailie.onboarding_presentation.nutrient_goal.NutrientGoalScreen
import com.andreeailie.onboarding_presentation.weight.WeightScreen
import com.andreeailie.onboarding_presentation.welcome.WelcomeScreen
import com.andreeailie.tracker_presentation.groceries_list.GroceriesListScreen
import com.andreeailie.tracker_presentation.search.SearchScreen
import com.andreeailie.tracker_presentation.tracker_overview.TrackerOverviewScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferences: Preferences

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val shouldShowOnboarding = preferences.loadShouldShowOnboarding()
        setContent {
            NutriNinjaTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                Scaffold(
                    scaffoldState = scaffoldState,
                    bottomBar = {
                        if (!shouldShowOnboarding) {
                            BottomNavigation(navController = navController)
                        }
                    }
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = if (shouldShowOnboarding) {
                            Route.WELCOME
                        } else Route.TRACKER_OVERVIEW
                    ) {
                        composable(Route.WELCOME) {
                            WelcomeScreen(onNavigate = navController::navigate)
                        }
                        composable(Route.AGE) {
                            AgeScreen(
                                scaffoldState = scaffoldState,
                                onNavigate = navController::navigate
                            )
                        }
                        composable(Route.GENDER) {
                            GenderScreen(onNavigate = navController::navigate)
                        }
                        composable(Route.HEIGHT) {
                            HeightScreen(
                                scaffoldState = scaffoldState,
                                onNavigate = navController::navigate
                            )
                        }
                        composable(Route.WEIGHT) {
                            WeightScreen(
                                scaffoldState = scaffoldState,
                                onNavigate = navController::navigate
                            )
                        }
                        composable(Route.NUTRIENT_GOAL) {
                            NutrientGoalScreen(
                                scaffoldState = scaffoldState,
                                onNavigate = navController::navigate
                            )
                        }
                        composable(Route.ACTIVITY) {
                            ActivityScreen(onNavigate = navController::navigate)
                        }
                        composable(Route.GOAL) {
                            GoalScreen(onNavigate = navController::navigate)
                        }
                        composable(Route.TRACKER_OVERVIEW) {
                            TrackerOverviewScreen(onNavigate = navController::navigate)
                        }
                        composable(
                            route = Route.SEARCH + "/{mealName}/{dayOfMonth}/{month}/{year}",
                            arguments = listOf(
                                navArgument("mealName") {
                                    type = NavType.StringType
                                },
                                navArgument("dayOfMonth") {
                                    type = NavType.IntType
                                },
                                navArgument("month") {
                                    type = NavType.IntType
                                },
                                navArgument("year") {
                                    type = NavType.IntType
                                },
                            )
                        ) {
                            val mealName = it.arguments?.getString("mealName")!!
                            val dayOfMonth = it.arguments?.getInt("dayOfMonth")!!
                            val month = it.arguments?.getInt("month")!!
                            val year = it.arguments?.getInt("year")!!
                            SearchScreen(
                                scaffoldState = scaffoldState,
                                mealName = mealName,
                                dayOfMonth = dayOfMonth,
                                month = month,
                                year = year,
                                onNavigateUp = {
                                    navController.navigateUp()
                                }
                            )
                        }
                        composable(Route.GROCERIES_LIST) {
                            GroceriesListScreen(onNavigate = navController::navigate)
                        }
//                        composable(Route.ADD_GROCERY) {
//                            AddEditGroceryScreen(onNavigate = navController::navigate)
//                        }
                    }
                }
            }
        }
    }
}