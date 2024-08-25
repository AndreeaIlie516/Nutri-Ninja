package com.andreeailie.tracker_data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andreeailie.tracker_data.local.entity.IngredientEntity
import com.andreeailie.tracker_data.local.entity.RecipeEntity
import com.andreeailie.tracker_data.local.entity.RecipeIngredientCrossRef

@Database(
    entities = [RecipeEntity::class, IngredientEntity::class, RecipeIngredientCrossRef::class],
    version = 1
)
abstract class RecipeDatabase : RoomDatabase() {

    abstract val dao: RecipeDao
}