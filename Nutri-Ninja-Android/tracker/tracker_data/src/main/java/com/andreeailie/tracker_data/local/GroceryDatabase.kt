package com.andreeailie.tracker_data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andreeailie.tracker_data.local.entity.GroceryEntity

@Database(
    entities = [GroceryEntity::class],
    version = 1
)
abstract class GroceryDatabase : RoomDatabase() {

    abstract val dao: GroceryDao
}