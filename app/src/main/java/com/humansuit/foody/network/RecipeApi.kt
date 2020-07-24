package com.humansuit.foody.network

import javax.inject.Inject

class RecipeApi @Inject constructor(private val recipeService: RecipeService) {

    suspend fun fetchRecipes(type: String) = recipeService.getRecipesByType(type = type)

    suspend fun fetchPopularRecipes(number: Int) = recipeService.getPopularRecipes(number = number)

}