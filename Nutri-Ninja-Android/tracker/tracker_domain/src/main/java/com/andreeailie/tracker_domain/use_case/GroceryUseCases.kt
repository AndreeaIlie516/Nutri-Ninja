package com.andreeailie.tracker_domain.use_case

data class GroceryUseCases(
    val addGrocery: AddGrocery,
    val deleteGrocery: DeleteGrocery,
    val searchGrocery: SearchGrocery,
    val getGroceries: GetGroceries,
    val toggleGroceryStatus: ToggleGroceryStatus
)