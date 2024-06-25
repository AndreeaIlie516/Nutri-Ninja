package com.andreeailie.tracker_presentation.tracker_overview.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andreeailie.core.R
import com.andreeailie.core_ui.LocalSpacing
import com.andreeailie.tracker_domain.model.TrackedFood
import com.andreeailie.tracker_presentation.tracker_overview.Meal

@Composable
fun MealItems(
    meals: List<Meal>,
    trackedFoods: List<TrackedFood>,
    onToggleMealClick: (Meal) -> Unit,
    onDeleteTrackedFoodClick: (TrackedFood) -> Unit,
    onAddFoodClick: (Meal) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    Box(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(30.dp))
            .clip(RoundedCornerShape(50.dp))
            .padding(top = 15.dp)
    ) {
        Column(modifier = modifier) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Your Meals",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp
                )
            }
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            meals.forEach { meal ->
                ExpandableMeal(
                    meal = meal,
                    onToggleClick = { onToggleMealClick(meal) },
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                            //.padding(horizontal = spacing.spaceExtraSmall)
                        ) {
                            trackedFoods
                                .filter { it.mealType == meal.mealType }
                                .forEach { food ->
                                    DeleteTrackedFood(
                                        trackedFood = food,
                                        onDeleteTrackedFoodClick = { onDeleteTrackedFoodClick(food) })
//                                    TrackedFoodItem(
//                                        trackedFood = food,
//                                        onDeleteClick = { onDeleteTrackedFoodClick(food) }
//                                    )
                                    Spacer(modifier = Modifier.height(spacing.spaceMedium))
                                }
                            AddButton(
                                text = stringResource(
                                    id = R.string.add_meal,
                                    meal.name.asString(LocalContext.current)
                                ),
                                onClick = { onAddFoodClick(meal) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = spacing.spaceSmall)
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
