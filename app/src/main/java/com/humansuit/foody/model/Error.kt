package com.humansuit.foody.model

data class Error(
    val message: String,
    val icon: Int,
    val recipeLoadingState: RecipeLoadingState
)