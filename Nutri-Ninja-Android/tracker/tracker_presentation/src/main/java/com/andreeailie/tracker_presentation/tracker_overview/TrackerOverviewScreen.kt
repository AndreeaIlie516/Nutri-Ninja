package com.andreeailie.tracker_presentation.tracker_overview

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andreeailie.core.R
import com.andreeailie.core.util.UiEvent
import com.andreeailie.core_ui.BackgroundLightGreen
import com.andreeailie.core_ui.LocalSpacing
import com.andreeailie.tracker_presentation.tracker_overview.components.DaySelector
import com.andreeailie.tracker_presentation.tracker_overview.components.MealItems
import com.andreeailie.tracker_presentation.tracker_overview.components.NutrientsBox
import com.andreeailie.tracker_presentation.tracker_overview.components.UserHeader

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TrackerOverviewScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: TrackerOverviewViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state
    Log.d("TrackerOverviewScreen", "trackedFoods: ${state.trackedFoods}")
    val context = LocalContext.current
    LaunchedEffect(key1 = context) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLightGreen)
            .padding(top = spacing.spaceLarge, bottom = 70.dp)
    ) {
        item {
            UserHeader(
                userPhoto = painterResource(id = R.drawable.profile),
                userName = "Andreea",
                state
            )
            Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))
            DaySelector(
                date = state.date,
                onPreviousDayClick = {
                    viewModel.onEvent(TrackerOverviewEvent.OnPreviousDayClick)
                },
                onNextDayClick = {
                    viewModel.onEvent(TrackerOverviewEvent.OnNextDayClick)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.spaceMedium)
            )
            Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))
            NutrientsBox(state = state)
            Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))
        }
        item {
            MealItems(
                meals = state.meals,
                trackedFoods = state.trackedFoods,
                onToggleMealClick = { meal ->
                    viewModel.onEvent(
                        TrackerOverviewEvent.OnToggleMealClick(
                            meal
                        )
                    )
                },
                onDeleteTrackedFoodClick = { food ->
                    viewModel.onEvent(
                        TrackerOverviewEvent.OnDeleteTrackedFoodClick(
                            food
                        )
                    )
                },
                onAddFoodClick = { meal ->
                    viewModel.onEvent(
                        TrackerOverviewEvent.OnAddFoodClick(
                            meal
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.spaceMedium)
            )
        }
    }
}