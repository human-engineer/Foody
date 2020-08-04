package com.humansuit.foody.network

import javax.inject.Inject

class RecipeApi @Inject constructor(private val recipeService: RecipeService) {

    suspend fun fetchRecipesByType(type: String, page: Int) = recipeService.getRecipesByType(type, page)

    suspend fun fetchPopularRecipes(number: Int) = recipeService.getPopularRecipes(number)

}