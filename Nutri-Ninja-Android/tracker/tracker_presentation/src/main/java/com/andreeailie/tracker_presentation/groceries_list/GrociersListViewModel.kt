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
            // Define the date range (last 7 days)
            val lastWeekDates = (0..6).map { LocalDate.now().minusDays(it.toLong()) }

            // Collect all foods tracked during the last week
            val foodItems = mutableListOf<TrackedFood>()
            for (date in lastWeekDates) {
                val foodsForDate =
                    trackerUseCases.getFoodsForDate(date).firstOrNull() ?: emptyList()
                foodItems.addAll(foodsForDate)
            }

            Log.d("GroceryListViewModel", "food items: $foodItems")

            // Group the foods by name and calculate the frequency
            val groupedFoods = foodItems.groupBy { it.name }
            val sortedFoodItems = groupedFoods.entries
                .sortedByDescending { it.value.sumOf { food -> food.quantity } }
                .map { it.key }

            Log.d("GroceryListViewModel", "sorted food items: $sortedFoodItems")
            // Set to avoid duplicate groceries
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
                            insertIfNotExists(grocery)
                        }
                    }
                }
            }

            refreshGroceries()
        }
    }


    private fun insertIfNotExists(grocery: Grocery) {
        viewModelScope.launch {
            val existingGrocery = groceryUseCases.getGroceries().firstOrNull()
                ?.find { it.name == grocery.name }
            if (existingGrocery == null) {
                Log.d("GroceryListViewModel", "grocery added: $grocery")
                groceryUseCases.addGrocery(grocery)
            }
        }
    }

    private fun extractQuantityAndUnit(description: String): Pair<Float, String> {
        // Define a regular expression to match quantity and unit
        val regex = """^(\d+\/\d+|\d+(\.\d+)?|\d+ \d+\/\d+)\s(\w+)\b""".toRegex()

        // Find the first match
        val matchResult = regex.find(description)

        return if (matchResult != null) {
            // Extract quantity and unit from match groups
            val quantityString = matchResult.groupValues[1]
            val unit = matchResult.groupValues[3]
            val quantity = parseQuantity(quantityString)
            quantity to unit
        } else {
            // Return default values if no match is found
            1f to "unit" // Default values in case of failure to match
        }
    }

    private fun parseQuantity(quantityString: String): Float {
        return if (quantityString.contains("/")) {
            // Handle fractions, e.g., "1/2"
            val parts = quantityString.split("/")
            if (parts.size == 2) {
                val numerator = parts[0].toFloatOrNull() ?: return 1f
                val denominator = parts[1].toFloatOrNull() ?: return 1f
                numerator / denominator
            } else {
                1f
            }
        } else {
            // Handle regular numbers, e.g., "1", "2.5"
            quantityString.toFloatOrNull() ?: 1f
        }
    }


}