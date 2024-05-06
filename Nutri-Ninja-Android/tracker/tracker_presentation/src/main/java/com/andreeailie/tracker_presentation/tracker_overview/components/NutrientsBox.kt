package com.andreeailie.tracker_presentation.tracker_overview.components


import androidx.compose.animation.core.animateIntAsState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.andreeailie.core_ui.LocalSpacing
import com.andreeailie.core.R
import androidx.compose.foundation.layout.*
import androidx.compose.ui.res.painterResource
import com.andreeailie.core_ui.*
import com.andreeailie.tracker_presentation.tracker_overview.TrackerOverviewState

@Composable
fun NutrientsBox(
    state: TrackerOverviewState,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    val animatedCalorieCount = animateIntAsState(
        targetValue = state.totalCalories, label = ""
    )
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = spacing.spaceExtraSmall,
                end = spacing.spaceExtraSmall,
                top = spacing.spaceExtraSmall,
                bottom = spacing.spaceExtraSmall
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .padding(
                    horizontal = spacing.spaceExtraSmall,
                    vertical = spacing.spaceExtraSmall
                )
                .fillMaxWidth(),
        ) {
            NutrientBarInfo(
                value = state.totalCalories,
                goal = state.caloriesGoal,
                name = stringResource(id = R.string.calories),
                unit = stringResource(id = R.string.kcal),
                color = CaloriesColor,
                backgroundColor = CaloriesColorBackground,
                iconResource = painterResource(id = R.drawable.ic_calories_cute_transp),
                modifier = Modifier.size(190.dp)
            )
            NutrientBarInfo(
                value = state.totalProtein,
                goal = state.proteinGoal,
                name = stringResource(id = R.string.protein),
                unit = stringResource(id = R.string.grams),
                color = ProteinColor,
                backgroundColor = ProteinColorBackground,
                iconResource = painterResource(id = R.drawable.ic_protein_cute_transp),
                modifier = Modifier.size(190.dp)
            )
        }
        //Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))
        Row(
            modifier = Modifier
                .padding(
                    horizontal = spacing.spaceExtraSmall,
                    vertical = spacing.spaceExtraSmall
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            NutrientBarInfo(
                value = state.totalCarbs,
                goal = state.carbsGoal,
                name = stringResource(id = R.string.carbs),
                unit = stringResource(id = R.string.grams),
                color = CarbColor,
                backgroundColor = CarbColorBackground,
                iconResource = painterResource(id = R.drawable.ic_carbs_cute_transp),
                modifier = Modifier.size(190.dp)
            )
            NutrientBarInfo(
                value = state.totalFat,
                goal = state.fatGoal,
                name = stringResource(id = R.string.fat),
                unit = stringResource(id = R.string.grams),
                color = FatColor,
                backgroundColor = FatColorBackground,
                iconResource = painterResource(id = R.drawable.ic_fats_cute_transp),
                modifier = Modifier.size(190.dp)
            )
        }
    }
}