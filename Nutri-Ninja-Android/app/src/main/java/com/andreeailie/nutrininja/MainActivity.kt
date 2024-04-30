package com.andreeailie.nutrininja

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.rememberScaffoldState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.andreeailie.core.navigation.Route
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
import com.andreeailie.tracker_presentation.tracker_overview.TrackerOverviewScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NutriNinjaTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                NavHost(
                    navController = navController,
                    startDestination = Route.WELCOME
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
                    composable(Route.SEARCH) {

                    }
                }
            }
        }
    }
}