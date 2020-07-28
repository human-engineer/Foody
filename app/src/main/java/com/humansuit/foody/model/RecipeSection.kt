package com.humansuit.foody.model

data class RecipeSection(
    val id: Int,
    val title: String,
    val recipes: List<Recipe>
)