package com.humansuit.foody.network

import javax.inject.Inject

class RecipeApi @Inject constructor(private val recipeService: RecipeService) {

    suspend fun fetchRecipesByType(
        number: Int = 10,
        page: Int = 0,
        type: String = "breakfast"
    ) = recipeService.getRecipesByType(type, page = page * 10, number = number)

    suspend fun fetchPopularRecipes(
        number: Int = 10,
        page: Int = 0
    ) = recipeService.getPopularRecipes(number, page = page * 10)

}