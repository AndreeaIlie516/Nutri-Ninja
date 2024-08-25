package com.andreeailie.tracker_presentation.groceries_list.add_grocery

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreeailie.tracker_domain.model.Grocery
import com.andreeailie.tracker_domain.model.InvalidGroceryException
import com.andreeailie.tracker_domain.use_case.GroceryUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditGroceryViewModel
@Inject
constructor(
    private val groceryUseCases: GroceryUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _groceryName = mutableStateOf(
        GroceryTextFieldState(
            hint = "Enter name"
        )
    )

    val groceryName: State<GroceryTextFieldState> = _groceryName

    private val _groceryUnit = mutableStateOf(
        GroceryTextFieldState(
            hint = "Enter unit"
        )
    )

    val groceryUnit: State<GroceryTextFieldState> = _groceryUnit

    private val _eventQuantity = mutableStateOf(
        GroceryTextFieldState(
            hint = "Enter quantity"
        )
    )

    val eventQuantity: State<GroceryTextFieldState> = _eventQuantity

    private val _eventImageUrl = mutableStateOf(
        GroceryTextFieldState(
            hint = "Enter image url"
        )
    )

    val eventImageUrl: State<GroceryTextFieldState> = _eventImageUrl

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var currentGroceryId: Int? = null


    fun onEvent(grocery: AddEditGroceryEvent) {

        when (grocery) {
            is AddEditGroceryEvent.EnteredName -> {
                _groceryName.value = groceryName.value.copy(
                    text = grocery.value
                )
            }

            is AddEditGroceryEvent.ChangeNameFocus -> {
                _groceryName.value = groceryName.value.copy(
                    isHintVisible = !grocery.focusState.isFocused &&
                            groceryName.value.text.isBlank()
                )
            }

            is AddEditGroceryEvent.EnteredUnit -> {
                _groceryUnit.value = groceryUnit.value.copy(
                    text = grocery.value
                )
            }

            is AddEditGroceryEvent.ChangeUnitFocus -> {
                _groceryUnit.value = groceryUnit.value.copy(
                    isHintVisible = !grocery.focusState.isFocused &&
                            groceryUnit.value.text.isBlank()
                )
            }

            is AddEditGroceryEvent.EnteredQuantity -> {
                _eventQuantity.value = eventQuantity.value.copy(
                    text = grocery.value
                )
            }

            is AddEditGroceryEvent.ChangeQuantityFocus -> {
                _eventQuantity.value = eventQuantity.value.copy(
                    isHintVisible = !grocery.focusState.isFocused &&
                            eventQuantity.value.text.isBlank()
                )

            }

            is AddEditGroceryEvent.EnteredImageUrl -> {
                _eventImageUrl.value = eventImageUrl.value.copy(
                    text = grocery.value
                )
            }

            is AddEditGroceryEvent.ChangeImageUrlFocus -> {
                _eventImageUrl.value = eventImageUrl.value.copy(
                    isHintVisible = !grocery.focusState.isFocused &&
                            eventImageUrl.value.text.isBlank()
                )
            }

            is AddEditGroceryEvent.SaveNewEvent -> {
                Log.i("AddEditEventViewModel", "Save new event current event id: $currentGroceryId")
                viewModelScope.launch {
                    try {
                        val grocery = Grocery(
                            id = 0,
                            name = groceryName.value.text,
                            unit = groceryUnit.value.text,
                            quantity = eventQuantity.value.text.toFloat(),
                            imageUrl = eventImageUrl.value.text,
                            isChecked = false
                        )

                        groceryUseCases.addGrocery(grocery)
                        _eventFlow.emit(UiEvent.SaveNewEvent)
                    } catch (e: InvalidGroceryException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save grocery"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data object SaveNewEvent : UiEvent()
    }
}