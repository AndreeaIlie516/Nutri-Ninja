package com.andreeailie.tracker_presentation.groceries_list

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andreeailie.core.R
import com.andreeailie.core.util.UiEvent
import com.andreeailie.core_ui.BackgroundLightGreen
import com.andreeailie.core_ui.LocalSpacing
import com.andreeailie.tracker_presentation.groceries_list.components.AddGroceryButton
import com.andreeailie.tracker_presentation.groceries_list.components.DeleteGrocery

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GroceriesListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: GroceryListViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val context = LocalContext.current
    LaunchedEffect(key1 = context) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }
    Log.d("GroceriesListScreen", "groceries: ${state.groceries}")
    Box {
        Row(
            modifier = Modifier
                .padding()
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column()
            {
                Text(
                    text = stringResource(id = R.string.groceries_list_screen),
                    textAlign = TextAlign.Left,
                    color = colorResource(R.color.black),
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLightGreen)
                .padding(top = spacing.spaceLarge, bottom = 70.dp)
        ) {
            items(state.groceries) { item ->
                DeleteGrocery(
                    modifier = Modifier,
                    grocery = item,
                    onDeleteGroceryClick = {
                        viewModel.onEvent(
                            GroceryListEvent.OnDeleteGroceryClick(
                                item
                            )
                        )
                    },
                    onCheckGroceryClick = {
                        viewModel.onEvent(
                            GroceryListEvent.OnCheckGroceryClick(
                                item
                            )
                        )
                    }
                )
            }
        }
        AddGroceryButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 45.dp, end = 10.dp),
            onClick = {
                viewModel.onEvent(
                    GroceryListEvent.OnAddGroceryClick
                )
            }
        )
    }

}