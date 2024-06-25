package com.andreeailie.tracker_presentation.tracker_overview.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.andreeailie.tracker_domain.model.TrackedFood
import com.andreeailie.tracker_presentation.components.DismissBackground
import com.andreeailie.tracker_presentation.components.ShowConfirmationDialog


@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@Composable
fun DeleteTrackedFood(
    modifier: Modifier = Modifier,
    trackedFood: TrackedFood,
    //onClickEditEvent: () -> Unit,
    onDeleteTrackedFoodClick: (TrackedFood) -> Unit,
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var show by remember { mutableStateOf(true) }
    val dismissState = rememberDismissState(
        confirmValueChange = {
            it == DismissValue.DismissedToStart
        }, positionalThreshold = { 150.dp.toPx() }
    )
    AnimatedVisibility(
        show, exit = fadeOut(spring())
    ) {
        SwipeToDismiss(
            state = dismissState,
            modifier = Modifier,
            background = {
                DismissBackground(dismissState)
            },
            dismissContent = {
                TrackedFoodItem(
                    trackedFood = trackedFood,
                    //onDeleteClick = { onDeleteTrackedFoodClick(trackedFood) }
                    //onClickEditEvent = onClickEditEvent
                )
            }
        )
    }

    LaunchedEffect(key1 = dismissState.currentValue) {
        if (dismissState.currentValue == DismissValue.DismissedToStart) {
            showDialog = true
            dismissState.reset()
        }
    }

    if (showDialog) {
        ShowConfirmationDialog(onConfirm = {
            onDeleteTrackedFoodClick(trackedFood)
            Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show()
            showDialog = false
            show = false
        }, onDismiss = {
            showDialog = false
        })
    }
}