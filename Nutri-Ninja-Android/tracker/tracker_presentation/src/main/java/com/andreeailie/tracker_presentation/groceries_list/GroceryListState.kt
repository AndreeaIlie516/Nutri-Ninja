package com.andreeailie.tracker_presentation.groceries_list

import android.os.Build
import androidx.annotation.RequiresApi
import com.andreeailie.tracker_domain.model.Grocery

data class GroceryListState @RequiresApi(Build.VERSION_CODES.O) constructor(
    val groceries: List<Grocery> = emptyList()
)