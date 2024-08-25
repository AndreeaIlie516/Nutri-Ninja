package com.andreeailie.tracker_data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andreeailie.tracker_data.local.entity.GroceryEntity
import com.andreeailie.tracker_data.local.entity.RecipeEntity
import com.andreeailie.tracker_data.local.entity.TrackedFoodEntity

@Database(
    entities = [TrackedFoodEntity::class, GroceryEntity::class, RecipeEntity::class],
    version = 1
)
abstract class NutriNinjaDatabase : RoomDatabase() {

    abstract val dao: NutriNinjaDao
}