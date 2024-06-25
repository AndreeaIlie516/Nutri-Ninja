package com.andreeailie.tracker_presentation.groceries_list

import android.os.Build
import androidx.annotation.RequiresApi
import com.andreeailie.tracker_domain.model.Grocery

data class GroceryListState @RequiresApi(Build.VERSION_CODES.O) constructor(
    val groceries: List<Grocery> = listOf(
        Grocery(
            name = "Eggs",
            quantity = 10,
            unit = "pcs",
            isChecked = false,
            imageUrl = "bhsdbf"
        ),
        Grocery(
            name = "Avocado",
            quantity = 1,
            unit = "pcs",
            isChecked = false,
            imageUrl = "bhsdbf"
        ),
        Grocery(
            name = "Pasta",
            quantity = 1,
            unit = "pcs",
            isChecked = false,
            imageUrl = "bhsdbf"
        ),
        Grocery(
            name = "Bacon",
            quantity = 1,
            unit = "pcs",
            isChecked = false,
            imageUrl = "bhsdbf"
        )
    )
)