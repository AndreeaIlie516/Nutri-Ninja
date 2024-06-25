package com.andreeailie.tracker_presentation.groceries_list

import com.andreeailie.tracker_domain.model.Grocery

sealed class GroceryListEvent {
    data class OnDeleteGroceryClick(val grocery: Grocery) : GroceryListEvent()
    data object OnAddGroceryClick : GroceryListEvent()
    data class OnCheckGroceryClick(val grocery: Grocery) : GroceryListEvent()
}