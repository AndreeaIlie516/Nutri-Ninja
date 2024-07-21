package com.andreeailie.tracker_presentation.search

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.andreeailie.core.R
import com.andreeailie.core.util.UiEvent
import com.andreeailie.core_ui.LocalSpacing
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalCoilApi::class)
@ExperimentalComposeUiApi
@Composable
fun FileUploadScreen(
    scaffoldState: ScaffoldState,
    mealName: String,
    dayOfMonth: Int,
    month: Int,
    year: Int,
    onNavigateUp: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
    fileUploadViewModel: FileUploadViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(key1 = keyboardController) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {

                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message.asString(context)
                    )
                    keyboardController?.hide()
                }

                is UiEvent.NavigateUp -> onNavigateUp()
                else -> Unit
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.spaceMedium)
    ) {
        Text(
            text = stringResource(id = R.string.add_meal, mealName),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(spacing.spaceMedium))
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = {
                val file = File(context.cacheDir, "image.jpeg")
                file.createNewFile()
                file.outputStream().use {
                    context.assets.open("carbonara.jpeg").copyTo(it)
                }
                Log.d("SearchScreen", file.absolutePath)
                fileUploadViewModel.uploadImage(file)
            }) {
                Text(text = "Upload image")
            }
        }
//        SearchTextField(
//            text = state.query,
//            onValueChange = {
//                viewModel.onEvent(SearchEvent.OnQueryChange(it))
//            },
//            shouldShowHint = state.isHintVisible,
//            onSearch = {
//                keyboardController?.hide()
//                viewModel.onEvent(SearchEvent.OnSearch)
//            },
//            onFocusChanged = {
//                viewModel.onEvent(SearchEvent.OnSearchFocusChange(it.isFocused))
//            }
//        )
//        Spacer(modifier = Modifier.height(spacing.spaceMedium))
//        LazyColumn(modifier = Modifier.fillMaxSize()) {
//            items(state.trackableFood) { food ->
//                TrackableFoodItem(
//                    trackableFoodUiState = food,
//                    onClick = {
//                        viewModel.onEvent(SearchEvent.OnToggleTrackableFood(food.food))
//                    },
//                    onAmountChange = {
//                        viewModel.onEvent(
//                            SearchEvent.OnAmountForFoodChange(
//                                food.food, it
//                            )
//                        )
//                    },
//                    onTrack = {
//                        keyboardController?.hide()
//                        viewModel.onEvent(
//                            SearchEvent.OnTrackFoodClick(
//                                food = food.food,
//                                mealType = MealType.fromString(mealName),
//                                date = LocalDate.of(year, month, dayOfMonth)
//                            )
//                        )
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }
//        }
    }
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        when {
//            state.isSearching -> CircularProgressIndicator()
//            state.trackableFood.isEmpty() -> {
//                Text(
//                    text = stringResource(id = R.string.no_results),
//                    style = MaterialTheme.typography.bodySmall,
//                    textAlign = TextAlign.Center
//                )
//            }
//        }
//    }
}