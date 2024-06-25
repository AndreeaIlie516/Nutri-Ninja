package com.andreeailie.tracker_presentation.groceries_list.add_grocery

import androidx.compose.ui.focus.FocusState

sealed class AddEditGroceryEvent {
    data class EnteredName(val value: String) : AddEditGroceryEvent()
    data class ChangeNameFocus(val focusState: FocusState) : AddEditGroceryEvent()
    data class EnteredImageUrl(val value: String) : AddEditGroceryEvent()
    data class ChangeImageUrlFocus(val focusState: FocusState) : AddEditGroceryEvent()
    data class EnteredUnit(val value: String) : AddEditGroceryEvent()
    data class ChangeUnitFocus(val focusState: FocusState) : AddEditGroceryEvent()
    data class EnteredQuantity(val value: String) : AddEditGroceryEvent()
    data class ChangeQuantityFocus(val focusState: FocusState) : AddEditGroceryEvent()

    data object SaveNewEvent : AddEditGroceryEvent()
}