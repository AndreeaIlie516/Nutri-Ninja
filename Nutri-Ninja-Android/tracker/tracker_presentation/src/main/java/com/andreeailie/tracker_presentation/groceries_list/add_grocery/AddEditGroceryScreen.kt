//package com.andreeailie.tracker_presentation.groceries_list.add_grocery
//
//import android.annotation.SuppressLint
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.SnackbarHostState
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Alignment.Companion.BottomEnd
//import androidx.compose.ui.Alignment.Companion.Center
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalSoftwareKeyboardController
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavController
//import com.andreeailie.core.R
//import com.andreeailie.core.util.UiEvent
//import com.andreeailie.core_ui.ButtonGreen
//import kotlinx.coroutines.flow.collectLatest
//
//@Composable
//fun SetScreenTitle(
//    modifier: Modifier = Modifier,
//    screenTitle: String
//) {
//    Row(
//        modifier = modifier
//            .padding(start = 25.dp, top = 65.dp, bottom = 20.dp)
//            .fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically,
//    ) {
//
//        Column(
//            modifier = modifier
//        )
//        {
//            Text(
//                text = screenTitle,
//                textAlign = TextAlign.Left,
//                color = colorResource(R.color.black),
//                style = MaterialTheme.typography.headlineMedium,
//            )
//        }
//    }
//}
//
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@Composable
//fun AddEditGroceryScreen(
//    onNavigateUp: () -> Unit,
//    navController: NavController,
//    modifier: Modifier = Modifier,
//    viewModel: AddEditGroceryViewModel = hiltViewModel()
//) {
//    val nameState = viewModel.groceryName.value
//    val unitState = viewModel.groceryUnit.value
//    val quantityState = viewModel.eventQuantity.value
//    val imageUrlState = viewModel.eventImageUrl.value
//
//    val snackbarHostState = remember { SnackbarHostState() }
//
//    val groceryId = viewModel.currentGroceryId
//
//    val screenScope = "add"
//
//    val titleTextId = R.string.add_grocery_screen
//    val buttonTextId = R.string.add_grocery_button_label
//
//    val keyboardController = LocalSoftwareKeyboardController.current
//    LaunchedEffect(key1 = keyboardController) {
//        viewModel.ui.collectLatest { event ->
//            when (event) {
//                is AddEditGroceryViewModel.UiEvent.ShowSnackbar -> {
//                    snackbarHostState.showSnackbar(
//                        message = event.message
//                    )
//                }
//
//                is AddEditGroceryViewModel.UiEvent.SaveNewEvent -> {
//                    navController.navigateUp()
//                }
//
//                is UiEvent.NavigateUp -> onNavigateUp()
//                else -> Unit
//            }
//        }
//    }
//    Column(
//        modifier = modifier
//    )
//    {
//        SetScreenTitle(
//            modifier = modifier,
//            stringResource(id = titleTextId)
//        )
//        Box(modifier = Modifier.fillMaxSize()) {
//            Column(
//                modifier = modifier
//            ) {
//                OutlinedTextField(
//                    value = nameState.text,
//                    label = { Text("Name") },
//                    onValueChange = {
//                        viewModel.onEvent(AddEditGroceryEvent.EnteredName(it))
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp)
//                )
//
//                OutlinedTextField(
//                    value = unitState.text,
//                    label = { Text("Unit") },
//                    onValueChange = {
//                        viewModel.onEvent(AddEditGroceryEvent.EnteredUnit(it))
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp)
//                )
//
//                OutlinedTextField(
//                    value = quantityState.text,
//                    label = { Text("Quantity") },
//                    onValueChange = {
//                        viewModel.onEvent(AddEditGroceryEvent.EnteredQuantity(it))
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp)
//                )
//
//                OutlinedTextField(
//                    value = imageUrlState.text,
//                    label = { Text("ImageUrl") },
//                    onValueChange = {
//                        viewModel.onEvent(AddEditGroceryEvent.EnteredImageUrl(it))
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp)
//                )
//            }
//            Box(
//                modifier = Modifier
//                    .align(Center)
//                    .padding(top = 450.dp),
//                contentAlignment = BottomEnd
//            ) {
//                Button(
//                    modifier = modifier
//                        .fillMaxWidth()
//                        .height(50.dp)
//                        .padding(start = 15.dp, end = 15.dp)
//                        .align(BottomEnd),
//                    shape = AbsoluteRoundedCornerShape(
//                        topLeft = 15.dp,
//                        topRight = 15.dp,
//                        bottomLeft = 15.dp,
//                        bottomRight = 15.dp
//                    ),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = ButtonGreen
//                    ),
//                    onClick = {
//                        viewModel.onEvent(AddEditGroceryEvent.SaveNewEvent)
//                    }
//                ) {
//                    Text(
//
//                        text = stringResource(id = buttonTextId),
//                        color = colorResource(id = R.color.white),
//                        style = TextStyle(
//                            fontSize = 20.sp,
//                        ),
//                    )
//                }
//
//            }
//        }
//    }
//}