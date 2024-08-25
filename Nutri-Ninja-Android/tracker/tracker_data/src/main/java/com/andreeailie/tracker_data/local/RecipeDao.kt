package com.andreeailie.tracker_data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.andreeailie.tracker_data.local.entity.IngredientEntity
import com.andreeailie.tracker_data.local.entity.RecipeEntity
import com.andreeailie.tracker_data.local.entity.RecipeIngredientCrossRef
import com.andreeailie.tracker_data.local.entity.RecipeWithIngredients
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Dao
interface RecipeDao {


    @Query("SELECT * FROM recipes")
    fun getRecipes(): Flow<List<RecipeEntity>>

    @Transaction
    @Query("SELECT * FROM recipes WHERE recipeId = :recipeId")
    suspend fun getRecipeWithIngredients(recipeId: Int): RecipeWithIngredients

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: IngredientEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeIngredientCrossRef(crossRef: RecipeIngredientCrossRef)

    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)

    @Transaction
    suspend fun addRecipeWithIngredients(
        recipe: RecipeEntity,
        ingredients: List<IngredientEntity>
    ) {
        val recipeId = insertRecipe(recipe)
        ingredients.forEach { ingredient ->
            val ingredientId = insertIngredient(ingredient)
            insertRecipeIngredientCrossRef(
                RecipeIngredientCrossRef(
                    recipeId.toInt(),
                    ingredientId.toInt()
                )
            )
        }
    }

    @Transaction
    suspend fun getRecipesWithIngredients(): List<RecipeWithIngredients> {
        val recipes = getRecipes().first()
        return recipes.map { recipeEntity ->
            getRecipeWithIngredients(recipeEntity.recipeId)
        }
    }
}