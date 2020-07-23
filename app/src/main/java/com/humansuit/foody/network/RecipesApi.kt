package com.humansuit.foody.network

class RecipesApi(private val recipesService: RecipesService) {

    suspend fun fetchRecipes(type: String) = recipesService.getRecipesByType(type = type)

}