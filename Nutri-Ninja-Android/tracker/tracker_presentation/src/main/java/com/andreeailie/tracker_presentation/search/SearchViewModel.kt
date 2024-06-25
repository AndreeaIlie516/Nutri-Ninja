package com.andreeailie.tracker_presentation.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreeailie.core.R
import com.andreeailie.core.domain.use_case.FilterOutDigits
import com.andreeailie.core.util.UiEvent
import com.andreeailie.core.util.UiText
import com.andreeailie.tracker_domain.use_case.TrackerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val trackerUseCases: TrackerUseCases,
    private val filterOutDigits: FilterOutDigits
) : ViewModel() {

    var state by mutableStateOf(SearchState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnQueryChange -> {
                Log.d("SearchViewModel", "OnQueryChange")
                state = state.copy(query = event.query)
            }

            is SearchEvent.OnAmountForFoodChange -> {
                Log.d("SearchViewModel", "OnAmountFoodChange")
                state = state.copy(
                    trackableFood = state.trackableFood.map {
                        if (it.food == event.food) {
                            it.copy(amount = filterOutDigits(event.amount))
                        } else it
                    }
                )
            }

            is SearchEvent.OnSearch -> {
                Log.d("SearchViewModel", "OnSearch")
                executeSearch()
            }

            is SearchEvent.OnToggleTrackableFood -> {
                state = state.copy(
                    trackableFood = state.trackableFood.map {
                        if (it.food == event.food) {
                            it.copy(isExpanded = !it.isExpanded)
                        } else it
                    }
                )
            }

            is SearchEvent.OnSearchFocusChange -> {
                Log.d("SearchViewModel", "OnSearchFocusChange")
                state = state.copy(
                    isHintVisible = !event.isFocused && state.query.isBlank()
                )
            }

            is SearchEvent.OnTrackFoodClick -> {
                trackFood(event)
            }
        }
    }

    private fun executeSearch() {
        viewModelScope.launch {
            state = state.copy(
                isSearching = true,
                trackableFood = emptyList()
            )
            Log.d("SearchViewModel", "searchFood called")
            Log.d("SearchViewModel", "query: ${state.query}")
            trackerUseCases
                .searchFood(state.query)
                .onSuccess { foods ->
                    Log.d("SearchViewModel", "foods: $foods")
                    state = state.copy(
                        trackableFood = foods.map {
                            TrackableFoodUiState(it)
                        },
                        isSearching = false,
                        query = ""
                    )
                    Log.d("SearchViewModel", "searchFood on success")
                    Log.d("SearchViewModel", "state: $state")
                }
                .onFailure {
                    state = state.copy(isSearching = false)
                    _uiEvent.send(
                        UiEvent.ShowSnackbar(
                            UiText.StringResource(R.string.error_something_went_wrong)
                        )
                    )
                    Log.d("SearchViewModel", "searchFood on failure")
                }
        }
    }

    private fun trackFood(event: SearchEvent.OnTrackFoodClick) {
        Log.d("SearchViewModel", "trackFood")
        viewModelScope.launch {
            val uiState = state.trackableFood.find { it.food == event.food }
            Log.d("SearchViewModel", "uiState: $uiState")
            Log.d("SearchViewModel", "foodName: ${uiState?.food?.name}")
            Log.d("SearchViewModel", "quantity: ${uiState?.food?.quantity}")
            Log.d("SearchViewModel", "fat: ${uiState?.food?.fatPer100g}")
            Log.d("SearchViewModel", "unit: ${event.unit}")
            Log.d("SearchViewModel", "mealType: ${event.mealType}")
            Log.d("SearchViewModel", "date: ${event.date}")
            trackerUseCases.trackFood(
                foodName = uiState?.food?.name ?: return@launch,
                quantity = uiState.amount.toIntOrNull() ?: return@launch,
                unit = event.unit,
                mealType = event.mealType,
                date = event.date
            )
            _uiEvent.send(UiEvent.NavigateUp)
        }
    }
}