package com.andreeailie.tracker_data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.andreeailie.tracker_data.local.entity.GroceryEntity
import com.andreeailie.tracker_data.remote.dto.SearchedGrocery
import com.andreeailie.tracker_domain.model.Grocery

fun SearchedGrocery.toGrocery(): Grocery {
    return Grocery(
        name = name,
        imageUrl = imageUrl ?: "",
        unit = unit,
        quantity = quantity,
        isChecked = isChecked
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun GroceryEntity.toGrocery(): Grocery {
    return Grocery(
        name = name,
        imageUrl = imageUrl,
        unit = unit,
        quantity = quantity,
        isChecked = isChecked,
        id = id
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun Grocery.toGroceryEntity(): GroceryEntity {
    return GroceryEntity(
        name = name,
        imageUrl = imageUrl,
        unit = unit,
        quantity = quantity,
        isChecked = isChecked,
        id = id
    )
}