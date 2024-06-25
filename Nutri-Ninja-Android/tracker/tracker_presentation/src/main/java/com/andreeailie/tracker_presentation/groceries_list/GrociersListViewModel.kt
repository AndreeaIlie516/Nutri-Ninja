package com.andreeailie.tracker_presentation.groceries_list

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreeailie.core.navigation.Route
import com.andreeailie.core.util.UiEvent
import com.andreeailie.tracker_domain.use_case.GroceryUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class GroceryListViewModel @Inject constructor(
    private val groceryUseCases: GroceryUseCases,
) : ViewModel() {

    var state by mutableStateOf(GroceryListState())


    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var getGroceriesJob: Job? = null

    init {
        //refreshGroceries()
    }

    fun onEvent(event: GroceryListEvent) {
        when (event) {
            is GroceryListEvent.OnAddGroceryClick -> {
                viewModelScope.launch {
                    _uiEvent.send(
                        UiEvent.Navigate(
                            route = Route.NUTRIENT_GOAL
                        )
                    )
                }
            }


            is GroceryListEvent.OnDeleteGroceryClick -> {
                viewModelScope.launch {
                    groceryUseCases.deleteGrocery(event.grocery)
                    refreshGroceries()
                }
            }

            is GroceryListEvent.OnCheckGroceryClick -> {
                viewModelScope.launch {
                    groceryUseCases.toggleGroceryStatus(event.grocery)
                    refreshGroceries()
                }
            }
        }
    }

    private fun refreshGroceries() {
        getGroceriesJob?.cancel()
        getGroceriesJob = groceryUseCases
            .getGroceries()
            .onEach { groceries ->
                state = state.copy(
                    groceries
                )
            }
            .launchIn(viewModelScope)
    }
}