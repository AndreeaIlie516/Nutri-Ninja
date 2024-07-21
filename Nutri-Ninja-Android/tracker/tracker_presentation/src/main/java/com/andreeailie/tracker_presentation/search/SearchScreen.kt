package com.andreeailie.tracker_presentation.search

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import com.andreeailie.core.R
import com.andreeailie.core.util.UiEvent
import com.andreeailie.core_ui.ButtonGreen
import com.andreeailie.core_ui.LocalSpacing
import com.andreeailie.tracker_domain.model.MealType
import com.andreeailie.tracker_presentation.search.components.SearchTextField
import com.andreeailie.tracker_presentation.search.components.TrackableFoodItem
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalCoilApi::class, ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    scaffoldState: ScaffoldState,
    mealName: String,
    dayOfMonth: Int,
    month: Int,
    year: Int,
    onNavigateUp: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
    fileUploadViewModel: FileUploadViewModel = hiltViewModel(),
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val bottomSheetState =
        rememberBottomSheetScaffoldState(bottomSheetState = BottomSheetState(initialValue = BottomSheetValue.Collapsed))
    val uploadResponse by fileUploadViewModel.uploadResponse.observeAsState()
    var isUploadCompleted by remember { mutableStateOf(false) }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = it
            }
        }

    val identifiedItems = remember { mutableStateOf(mutableMapOf<String, String>()) }

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

    BottomSheetScaffold(
        modifier = Modifier.padding(bottom = 60.dp),
        scaffoldState = bottomSheetState,
        sheetContent = {
            if (!isUploadCompleted) {
                Text(
                    text = "Select image",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .padding(spacing.spaceSmall)
                        .align(Alignment.CenterHorizontally)
                )
                Button(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier
                        .padding(spacing.spaceMedium)
                        .align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonGreen)
                ) {
                    Text(text = "Open gallery")
                }
            }
        },
        sheetPeekHeight = 0.dp
    ) {
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
            SearchTextField(
                text = state.query,
                onValueChange = {
                    viewModel.onEvent(SearchEvent.OnQueryChange(it))
                },
                shouldShowHint = state.isHintVisible,
                onSearch = {
                    keyboardController?.hide()
                    viewModel.onEvent(SearchEvent.OnSearch)
                },
                onFocusChanged = {
                    viewModel.onEvent(SearchEvent.OnSearchFocusChange(it.isFocused))
                },
                onUpload = {
                    coroutineScope.launch {
                        bottomSheetState.bottomSheetState.expand()
                    }
                }
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.trackableFood) { food ->
                    TrackableFoodItem(
                        trackableFoodUiState = food,
                        onClick = {
                            viewModel.onEvent(SearchEvent.OnToggleTrackableFood(food.food))
                        },
                        onAmountChange = {
                            viewModel.onEvent(
                                SearchEvent.OnAmountForFoodChange(
                                    food.food, it
                                )
                            )
                        },
                        onTrack = {
                            keyboardController?.hide()
                            viewModel.onEvent(
                                SearchEvent.OnTrackFoodClick(
                                    food = food.food,
                                    mealType = MealType.fromString(mealName),
                                    date = LocalDate.of(year, month, dayOfMonth)
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .height(350.dp),
            contentAlignment = Alignment.Center
        ) {
            imageUri?.let {
                val painter = rememberAsyncImagePainter(model = it)
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.height(350.dp),
                    contentScale = ContentScale.Crop
                )
                uploadResponse?.let { response ->
                    if (response.success) {
                        response.results?.forEach { result ->
                            val tag =
                                identifiedItems.value.getOrDefault(result.`class`, result.`class`)
                            DrawAnimatedContours(result.coordinates)
                            DrawTag(tag, result.coordinates) { newTag ->
                                identifiedItems.value = identifiedItems.value.toMutableMap().apply {
                                    put(result.`class`, newTag)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(spacing.spaceMedium))
                        Button(
                            onClick = {
                                // Handle save action here
                                saveIdentifiedItems(identifiedItems.value)
                            },
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(top = 450.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ButtonGreen)
                        ) {
                            Text("Save")
                        }
                    }
                }
                if (!isUploadCompleted) {
                    Spacer(modifier = Modifier.height(spacing.spaceMedium))
                    Button(
                        onClick = {
                            imageUri?.let { uri ->
                                val file = getFileFromUri(context, uri)
                                if (file != null) {
                                    fileUploadViewModel.uploadImage(file)
                                    isUploadCompleted = true
                                }
                            }
                        },
                        modifier = Modifier
                            .padding(top = 450.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonGreen)
                    ) {
                        Text("Upload Image")
                    }
                }
            }
        }
    }
}

fun saveIdentifiedItems(identifiedItems: Map<String, String>) {
    // Implement your save logic here
    // For example, you could save the items to a database or send them to a server
    println("Saving items: $identifiedItems")
}

@Composable
fun DrawAnimatedContours(coordinates: List<Int>) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 150.dp, start = 50.dp)
    ) {
        val imageWidth = 350
        val imageHeight = 350
        val scaleX = size.width / imageWidth
        val scaleY = size.height / imageHeight

        val path = Path().apply {
            moveTo(coordinates[0] * scaleX, coordinates[1] * scaleY)
            for (i in 2 until coordinates.size step 2) {
                lineTo(coordinates[i] * scaleX, coordinates[i + 1] * scaleY)
            }
            close()
        }
        drawPath(path, Color.Red.copy(alpha = alpha), style = Stroke(width = 10f))
    }
}

@Composable
fun DrawTag(tag: String, coordinates: List<Int>, onTagChange: (String) -> Unit) {
    val imageWidth = 350
    val imageHeight = 350
    val scaleX = imageWidth.toFloat() / imageWidth
    val scaleY = imageHeight.toFloat() / imageHeight

    val offsetX = coordinates[0] * scaleX
    val offsetY = coordinates[1] * scaleY

    val tagWidth = 150.dp
    val tagHeight = 60.dp
    val pointerWidth = 20.dp
    val pointerHeight = 10.dp

    val spacing = LocalSpacing.current

    Box(
        modifier = Modifier
            .offset(x = offsetX.dp - tagWidth / 2, y = offsetY.dp - tagHeight - pointerHeight)
    ) {
        Canvas(
            modifier = Modifier
                .size(tagWidth, tagHeight + pointerHeight)
                .background(Color.Transparent)
        ) {
            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width, size.height - pointerHeight.toPx())
                lineTo(size.width / 2 + pointerWidth.toPx() / 2, size.height - pointerHeight.toPx())
                lineTo(size.width / 2, size.height)
                lineTo(size.width / 2 - pointerWidth.toPx() / 2, size.height - pointerHeight.toPx())
                lineTo(0f, size.height - pointerHeight.toPx())
                close()
            }
            drawPath(path, color = Color.White)
            drawPath(path, color = Color.Black, style = Stroke(width = 2.dp.toPx()))
        }
        Column(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            BasicTextField(
                value = tag,
                onValueChange = onTagChange,
                singleLine = true,
                modifier = Modifier
                    .background(Color.White)
                    .padding(end = spacing.spaceMedium)
            )
        }
    }
}


fun getFileFromUri(context: Context, uri: Uri): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val tempFile = File(context.cacheDir, "temp_image.jpg")
        tempFile.outputStream().use { output ->
            inputStream?.copyTo(output)
        }
        tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}