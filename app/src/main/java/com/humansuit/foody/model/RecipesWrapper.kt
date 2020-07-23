package com.humansuit.foody.model

data class RecipesWrapper(
    val results : List<Recipe>,
    val offset : Int,
    val number : Int,
    val totalResults : Int
)