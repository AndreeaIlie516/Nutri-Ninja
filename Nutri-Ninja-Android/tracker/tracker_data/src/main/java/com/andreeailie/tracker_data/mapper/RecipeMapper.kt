package com.andreeailie.tracker_data.mapper

import com.andreeailie.tracker_data.local.entity.IngredientEntity
import com.andreeailie.tracker_data.local.entity.RecipeEntity
import com.andreeailie.tracker_data.remote.dto.SearchedIngredient
import com.andreeailie.tracker_data.remote.dto.SearchedRecipe
import com.andreeailie.tracker_domain.model.Ingredient
import com.andreeailie.tracker_domain.model.Recipe

fun SearchedIngredient.toIngredient(): Ingredient {
    return Ingredient(
        name = name,
        description = description,
        imageUrl = imageUrl,
    )
}

fun SearchedRecipe.toRecipe(): Recipe {
    return Recipe(
        name = name,
        imageUrl = imageUrl,
        ingredients = ingredients.map { ingredientArray ->
            Ingredient(
                name = ingredientArray.getOrNull(0) ?: "",
                description = ingredientArray.getOrNull(1) ?: "",
                imageUrl = ingredientArray.getOrNull(2)
            )
        },
        servingSize = servingSize.toFloatOrNull() ?: 0f,
        servings = servings.toIntOrNull() ?: 0,
        instructions = instructions,
        description = recipeDescription
    )
}


fun RecipeEntity.toRecipe(ingredients: List<IngredientEntity>): Recipe {
    return Recipe(
        name = name,
        imageUrl = imageUrl,
        ingredients = ingredients.map { Ingredient(it.name, it.description, it.imageUrl) },
        servingSize = servingSize,
        servings = servings,
        instructions = instructions,
        description = description,
        id = recipeId
    )
}

fun Recipe.toRecipeEntity(): RecipeEntity {
    return RecipeEntity(
        name = name,
        imageUrl = imageUrl,
        servingSize = servingSize,
        servings = servings,
        instructions = instructions,
        description = description,
        recipeId = id ?: 0
    )
}

fun Recipe.toIngredientEntities(): List<IngredientEntity> {
    return ingredients.map {
        IngredientEntity(
            name = it.name,
            description = it.description,
            imageUrl = it.imageUrl
        )
    }
}

fun IngredientEntity.toIngredient(): Ingredient {
    return Ingredient(
        name = name,
        description = description,
        imageUrl = imageUrl
    )
}

fun Ingredient.toIngredientEntity(): IngredientEntity {
    return IngredientEntity(
        name = name,
        description = description,
        imageUrl = imageUrl
    )
}
