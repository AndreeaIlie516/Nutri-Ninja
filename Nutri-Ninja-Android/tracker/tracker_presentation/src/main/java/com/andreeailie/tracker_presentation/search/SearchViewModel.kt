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
import com.andreeailie.tracker_domain.model.MealType
import com.andreeailie.tracker_domain.model.TrackableFood
import com.andreeailie.tracker_domain.use_case.TrackerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

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
                state = state.copy(query = event.query)
            }

            is SearchEvent.OnAmountForFoodChange -> {
                state = state.copy(
                    trackableFood = state.trackableFood.map {
                        if (it.food == event.food) {
                            it.copy(amount = filterOutDigits(event.amount))
                        } else it
                    }
                )
            }

            is SearchEvent.OnSearch -> {
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
                state = state.copy(
                    isHintVisible = !event.isFocused && state.query.isBlank()
                )
            }

            is SearchEvent.OnTrackFoodClick -> {
                trackFood(event)
            }

            is SearchEvent.SaveIdentifiedItems -> {
                saveIdentifiedItems(event.identifiedItems, event.mealName, event.date, event.unit)
            }
        }
    }

    private fun executeSearch(
        query: String = state.query,
        onResult: (List<TrackableFood>) -> Unit = {}
    ) {
        Log.d("SearchViewModel", "executeSearch on query: $query")
        viewModelScope.launch {
            state = state.copy(
                isSearching = true,
                trackableFood = emptyList()
            )
            try {
                trackerUseCases
                    .searchFood(query)
                    .onSuccess { foods ->
                        Log.d("SearchViewModel", "executeSearch on success: $foods")
                        state = state.copy(
                            trackableFood = foods.map { TrackableFoodUiState(it) },
                            isSearching = false,
                            query = ""
                        )
                        onResult(foods)
                    }
                    .onFailure { error ->
                        Log.d("SearchViewModel", "executeSearch on failure: $error")
                        state = state.copy(isSearching = false)
                        _uiEvent.send(UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_something_went_wrong)))
                    }
            } catch (e: CancellationException) {
                Log.d("SearchViewModel", "executeSearch was cancelled: ${e.message}")
                state = state.copy(isSearching = false)
            } catch (e: Exception) {
                Log.d("SearchViewModel", "executeSearch on failure: ${e.message}")
                state = state.copy(isSearching = false)
                _uiEvent.send(UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_something_went_wrong)))
            }
        }
    }

    private fun trackFood(event: SearchEvent.OnTrackFoodClick) {
        Log.d("SearchViewModel", "onTrackFood")
        viewModelScope.launch {
            val uiState = state.trackableFood.find { it.food == event.food }
            trackerUseCases.trackFood(
                foodName = uiState?.food?.name ?: return@launch,
                quantity = uiState.amount.toIntOrNull() ?: 100,
                unit = event.unit,
                mealType = event.mealType,
                date = event.date
            )
            _uiEvent.send(UiEvent.NavigateUp)
        }
    }

    private fun trackIdentifiedFood(event: SearchEvent.OnTrackFoodClick) {
        Log.d("SearchViewModel", "onTrackFood")
        viewModelScope.launch {
            val uiState = state.trackableFood.find { it.food == event.food }
            trackerUseCases.trackFood(
                foodName = event.food.name,
                quantity = event.food.quantity,
                unit = event.unit,
                mealType = event.mealType,
                date = event.date
            )
            _uiEvent.send(UiEvent.NavigateUp)
        }
    }

    private fun saveIdentifiedItems(
        identifiedItems: Map<String, Pair<String, Int>>,
        mealName: String,
        date: LocalDate,
        unit: String
    ) {
        viewModelScope.launch {
            Log.d("SearchViewModel", "identifiedItems: $identifiedItems")
            identifiedItems.forEach { (originalTag, pair) ->
                Log.d("SearchViewModel", "items: $pair")
                executeSearch(pair.first) { foods ->
                    if (foods.isNotEmpty()) {
                        val food = foods.first()
                        val newFood = food.copy(quantity = pair.second)
                        Log.d("SearchViewModel", "Tracking food: $newFood")
                        trackIdentifiedFood(
                            SearchEvent.OnTrackFoodClick(
                                food = newFood,
                                unit = unit,
                                mealType = MealType.fromString(mealName),
                                date = date
                            )
                        )
                    } else {
                        Log.d("SearchViewModel", "No food found for query: ${pair.first}")
                    }
                }
            }
        }
    }
}