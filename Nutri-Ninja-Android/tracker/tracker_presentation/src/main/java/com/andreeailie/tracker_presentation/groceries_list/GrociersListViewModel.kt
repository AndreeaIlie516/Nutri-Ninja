package com.andreeailie.tracker_presentation.groceries_list

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreeailie.core.navigation.Route
import com.andreeailie.core.util.UiEvent
import com.andreeailie.tracker_domain.model.Grocery
import com.andreeailie.tracker_domain.model.Ingredient
import com.andreeailie.tracker_domain.model.TrackedFood
import com.andreeailie.tracker_domain.use_case.GroceryUseCases
import com.andreeailie.tracker_domain.use_case.RecipeUseCases
import com.andreeailie.tracker_domain.use_case.TrackerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class GroceryListViewModel @Inject constructor(
    private val groceryUseCases: GroceryUseCases,
    private val trackerUseCases: TrackerUseCases,
    private val recipeUseCases: RecipeUseCases
) : ViewModel() {

    var state by mutableStateOf(GroceryListState())


    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var getGroceriesJob: Job? = null

    init {
        refreshGroceries()
        refreshFrequentGroceries()
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

    private fun clearGroceries() {
        viewModelScope.launch {
            groceryUseCases.getGroceries().firstOrNull()?.forEach { grocery ->
                groceryUseCases.deleteGrocery(grocery)
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

    private fun refreshFrequentGroceries() {
        Log.d("GroceryListViewModel", "refresh grocery")
        viewModelScope.launch {
            val lastWeekDates = (0..6).map { LocalDate.now().minusDays(it.toLong()) }
            val foodItems = mutableListOf<TrackedFood>()
            for (date in lastWeekDates) {
                val foodsForDate =
                    trackerUseCases.getFoodsForDate(date).firstOrNull() ?: emptyList()
                foodItems.addAll(foodsForDate)
            }
            Log.d("GroceryListViewModel", "food items: $foodItems")
            val groupedFoods = foodItems.groupBy { it.name }
            val sortedFoodItems = groupedFoods.entries
                .sortedByDescending { it.value.sumOf { food -> food.quantity } }
                .map { it.key }

            Log.d("GroceryListViewModel", "sorted food items: $sortedFoodItems")
            val uniqueIngredients = mutableSetOf<Ingredient>()

            for (foodItem in sortedFoodItems) {
                Log.d("GroceryListViewModel", "food item in sorted: $foodItem")
                val recipesResult = recipeUseCases.searchRecipe(foodItem)
                Log.d("GroceryListViewModel", "recipe result: $recipesResult")
                if (recipesResult.isSuccess) {
                    Log.d("GroceryListViewModel", "isSuccess: ${recipesResult.isSuccess}")
                    val recipes = recipesResult.getOrNull()
                    recipes?.firstOrNull()?.ingredients?.forEach { ingredient ->
                        Log.d("GroceryListViewModel", "ingredient: $ingredient")
                        if (uniqueIngredients.add(ingredient)) {
                            val (quantity, unit) = extractQuantityAndUnit(ingredient.description)
                            Log.d(
                                "GroceryListViewModel",
                                "extracted quantity: $quantity, unit: $unit"
                            )
                            val grocery = Grocery(
                                name = ingredient.name,
                                quantity = quantity,
                                unit = unit,
                                imageUrl = ingredient.imageUrl,
                                isChecked = false
                            )
                            Log.d("GroceryListViewModel", "grocery: $grocery")
                            insertOrUpdateGrocery(grocery)
                        }
                    }
                }
            }

            refreshGroceries()
        }
    }

    private fun insertOrUpdateGrocery(grocery: Grocery) {
        viewModelScope.launch {
            val existingGrocery = groceryUseCases.getGroceries().firstOrNull()
                ?.find { it.name == grocery.name && it.unit == grocery.unit }

            if (existingGrocery != null) {
                val updatedGrocery = existingGrocery.copy(
                    quantity = existingGrocery.quantity + grocery.quantity
                )
                Log.d("GroceryListViewModel", "grocery updated: $updatedGrocery")
                groceryUseCases.addGrocery(updatedGrocery)
            } else {
                Log.d("GroceryListViewModel", "grocery added: $grocery")
                groceryUseCases.addGrocery(grocery)
            }
        }
    }

    private fun extractQuantityAndUnit(description: String): Pair<Float, String> {
        val regex = """^(\d+\/\d+|\d+(\.\d+)?|\d+ \d+\/\d+)\s(\w+)\b""".toRegex()
        val matchResult = regex.find(description)

        return if (matchResult != null) {
            val quantityString = matchResult.groupValues[1]
            val unit = matchResult.groupValues[3]
            val quantity = parseQuantity(quantityString)
            quantity to unit
        } else {
            1f to "unit"
        }
    }

    private fun parseQuantity(quantityString: String): Float {
        return if (quantityString.contains("/")) {
            val parts = quantityString.split("/")
            if (parts.size == 2) {
                val numerator = parts[0].toFloatOrNull() ?: return 1f
                val denominator = parts[1].toFloatOrNull() ?: return 1f
                numerator / denominator
            } else {
                1f
            }
        } else {
            quantityString.toFloatOrNull() ?: 1f
        }
    }


}