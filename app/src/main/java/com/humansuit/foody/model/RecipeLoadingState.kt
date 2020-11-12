package com.humansuit.foody.model

data class RecipeLoadingState(
    val recipeSectionType: RecipeSectionType,
    val number: Int,
    val page: Int
)